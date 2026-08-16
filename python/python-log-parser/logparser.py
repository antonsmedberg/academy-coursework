import re
import json
import datetime

# =========================================================
# 1) REGEX-KONFIGURATION FÖR TEXTLOGG
# =========================================================
# Här har jag ett exempel på textloggformat:
# [YYYY-MM-DD HH:MM:SS] LEVEL MODULE REST
# Du kan byta ut TEXTLOG_PATTERN om ditt format skiljer sig.
TEXTLOG_PATTERN = re.compile(
    r'^\[(?P<date>[^]]+)\]\s+(?P<level>INFO|ERROR|WARN|DEBUG|CRITICAL)\s+(?P<module>\S+)\s+(?P<msg>.*)$'
)

# Dessa loggnivåer tycker jag är okej i textloggar
VALID_LOG_LEVELS = {"INFO", "ERROR", "WARN", "DEBUG", "CRITICAL"}


# =========================================================
# 2) PARSER-FUNKTIONER (TEXT, JSON, DATUM)
# =========================================================

def parse_textlog_line(line_content: str):
    """
    Jag försöker fatta om 'line_content' är en textlog av format:
      [YYYY-MM-DD HH:MM:SS] LEVEL MODULE MSG
    Om det matchar min regex, ger jag en dict med {date, level, module, msg}.
    Annars ger jag None, för då vet jag inte vad det är.
    """
    cleaned_line = line_content.strip()
    matched = TEXTLOG_PATTERN.match(cleaned_line)
    if not matched:
        return None
    
    date_str = matched.group("date")
    level_str = matched.group("level")
    module_str = matched.group("module")
    message_str = matched.group("msg")

    # Kollar om level_str finns bland mina godkända nivåer
    if level_str not in VALID_LOG_LEVELS:
        return None

    return {
        "date": date_str,
        "level": level_str,
        "module": module_str,
        "msg": message_str
    }


def parse_json_line(line_content: str):
    """
    Jag försöker läsa 'line_content' som JSON (dict).
    Om det går bra, returnerar jag en dict, om inte -> None.
    """
    line_str = line_content.strip()
    if not (line_str.startswith("{") and line_str.endswith("}")):
        return None
    
    try:
        parsed_json = json.loads(line_str)
        if isinstance(parsed_json, dict):
            return parsed_json
    except json.JSONDecodeError:
        pass
    return None


def try_parse_datetime(date_str: str):
    """
    Jag vill se om 'date_str' passar i några vanliga ISO-format, t.ex:
      YYYY-MM-DD HH:MM:SS
      YYYY-MM-DDTHH:MM:SS
      YYYY-MM-DD HH:MM
      YYYY-MM-DDTHH:MM
    Om jag lyckas -> datetime, annars -> None.
    """
    if not date_str:
        return None
    
    possible_formats = [
        "%Y-%m-%d %H:%M:%S",
        "%Y-%m-%dT%H:%M:%S",
        "%Y-%m-%d %H:%M",
        "%Y-%m-%dT%H:%M"
    ]
    for date_format in possible_formats:
        try:
            return datetime.datetime.strptime(date_str, date_format)
        except ValueError:
            pass
    return None


# =========================================================
# 3) PARSER-PIPELINE & MULTILINE
# =========================================================
# Jag har en samling parse-funktioner jag vill testa i ordning:
PARSER_FUNCTIONS = [
    parse_json_line,
    parse_textlog_line
    # parse_syslog_line, parse_my_special_format, ...
]

def parse_log_pipeline(lines_of_text):
    """
    Jag loopar över 'lines_of_text'. För varje rad:
      1) Testar jag alla parse-funktioner i PARSER_FUNCTIONS (i ordning).
      2) Om ingen parser ger en dict -> multiline (lägg rad i 'msg' på current_entry).
    Returnerar en lista av dict-poster. Varje kan ha 'dt' om jag kan läsa datumet.
    """
    parsed_entries = []
    current_entry = None

    # Går igenom alla rader en och en
    for raw_line in lines_of_text:
        line_str = raw_line.rstrip("\n")
        parse_result = None  # För att se om nån parser lyckats
        
        # Testar varje parse-funktion i tur och ordning
        for parser_func in PARSER_FUNCTIONS:
            parse_result = parser_func(line_str)
            if parse_result is not None:
                # Aha, vi fick en match => spara gammal 'current_entry' om den finns
                if current_entry:
                    parsed_entries.append(current_entry)
                current_entry = parse_result
                break  # Avbryt parser-loopen, vi är klara för denna rad

        # Om parse_result fortfarande är None => multiline
        if parse_result is None:
            if current_entry:
                current_entry["msg"] += "\n" + line_str
            else:
                # Skapa en ny "UNKNOWN" om vi inte har nån post igång
                current_entry = {
                    "date": "",
                    "level": "UNKNOWN",
                    "module": "",
                    "msg": line_str
                }

    # Efter raderna är slut, spara ev. 'current_entry'
    if current_entry:
        parsed_entries.append(current_entry)

    # Försök tolka 'date' -> 'dt' för varje post
    for log_dict in parsed_entries:
        dt_val = try_parse_datetime(log_dict.get("date", ""))
        log_dict["dt"] = dt_val

    return parsed_entries


# =========================================================
# 4) SORTERING & CHUNKING
# =========================================================

def sort_log_entries_by_datetime(log_list):
    """
    Jag sorterar log_list uppåt på 'dt', och de som saknar dt går sist.
    """
    log_list.sort(key=lambda log_item: log_item.get("dt") or datetime.datetime.max)
    return log_list

def chunk_log_entries(log_list, interval_minutes=5):
    """
    Jag klumpar ihop loggar i block om 'interval_minutes' var. 
    Loggar utan dt lägger jag i 'missing_datetime'-blocken.
    """
    if not log_list:
        return []

    sort_log_entries_by_datetime(log_list)

    blocks = []
    active_block = {"start_dt": None, "end_dt": None, "logs": []}
    missing_dt_block = {"start_dt": None, "end_dt": None, "logs": []}

    # Loopar igenom varje logpost och kollar vilken tidsklump den tillhör
    for log_obj in log_list:
        dt_field = log_obj.get("dt")
        if not dt_field:
            # Ingen dt => lägg i missing_dt_block
            missing_dt_block["logs"].append(log_obj)
            continue

        # Golvar ner tidsstämpelns minuter till närmsta 5-min-intervall
        floored_minute = (dt_field.minute // interval_minutes) * interval_minutes
        block_start = dt_field.replace(minute=floored_minute, second=0, microsecond=0)
        block_end = block_start + datetime.timedelta(minutes=interval_minutes)

        if active_block["start_dt"] is None:
            active_block["start_dt"] = block_start
            active_block["end_dt"] = block_end

        # Om blockets start_dt eller end_dt ändrats => nytt block
        if (active_block["start_dt"] != block_start) or (active_block["end_dt"] != block_end):
            blocks.append(active_block)
            active_block = {
                "start_dt": block_start,
                "end_dt": block_end,
                "logs": [log_obj]
            }
        else:
            active_block["logs"].append(log_obj)

    # Till sist: spara det aktiva blocket om det inte är tomt
    if active_block["logs"]:
        blocks.append(active_block)

    # Om vi har några loggar utan dt => in i missing_dt_block
    if missing_dt_block["logs"]:
        blocks.append(missing_dt_block)

    return blocks

# =========================================================
# 5) NORMALISERING AV RÅDATA
# =========================================================

def normalize_raw_log_data(raw_data):
    """
    Jag vill konvertera 'raw_data' till en lista av strängar.
    1) Om det är en list -> varje element -> str (dict -> json, str -> samma).
    2) Om det är en str -> kolla om det är JSON-lista ([...]). 
       - Om ja, parsea den listan
       - Annars -> dela upp i rader
    3) Allt annat -> str() 
    """
    if isinstance(raw_data, list):
        normalized_list = []
        # Går igenom alla element i listan
        for element in raw_data:
            if isinstance(element, dict):
                normalized_list.append(json.dumps(element))
            elif isinstance(element, str):
                normalized_list.append(element)
            else:
                normalized_list.append(str(element))
        return normalized_list

    if isinstance(raw_data, str):
        trimmed_str = raw_data.strip()
        # Kollar om det är en JSON-lista
        if trimmed_str.startswith("[") and trimmed_str.endswith("]"):
            try:
                loaded_data = json.loads(trimmed_str)
                if isinstance(loaded_data, list):
                    return normalize_raw_log_data(loaded_data)
                return [trimmed_str]
            except json.JSONDecodeError:
                # Om parse failar -> dela rader
                return raw_data.splitlines()
        else:
            return raw_data.splitlines()

    # Om ingenting matchar => str()
    return [str(raw_data)]

# =========================================================
# 6) HUVUDPROGRAM (DEMO)
# =========================================================

def main():
    """
    Jag visar här tre exempel på hur man kan köra parsern:
      1) Normalisera (str/list)
      2) Kör parse_log_pipeline (TEXT, JSON, multiline)
      3) Sortera & chunk
      4) Skriv ut i JSON

    Om du vill utöka:
      * parse_syslog_line -> PARSER_FUNCTIONS
      * Byta TEXTLOG_PATTERN
      * Fler datumformat i try_parse_datetime
    """

    # EXEMPEL 1: JSON-lik sträng
    example_data_1 = """
    [
      [2025-03-23 12:35:59] INFO MyModule Startar systemet
      [2025-03-23 12:36:00] ERROR AuthModule Fel vid inloggning:
      { "date": "2025-03-23T12:40:00", "level": "DEBUG", "module": "ConfigModule", "msg": "Konfiguration inläst" }
      Multiline fortsättning rad
      [2025-03-23 12:45:00] WARN NetModule Kanske nåt varning
    ]
    """
    lines_ex1 = normalize_raw_log_data(example_data_1)
    parsed_ex1 = parse_log_pipeline(lines_ex1)
    sort_log_entries_by_datetime(parsed_ex1)
    chunked_ex1 = chunk_log_entries(parsed_ex1, interval_minutes=5)

    print("\n=== Exempel 1 ===")
    print(json.dumps(chunked_ex1, indent=2, ensure_ascii=False, default=str))

    # EXEMPEL 2: Python-list (blandad data)
    example_data_2 = [
        "[2025-03-23 13:00:00] WARN NetModule Potential issue",
        {"date": "2025-03-23T13:05:12", "level": "ERROR", "module": "TestModule", "msg": "Något gick fel"},
        "Okänd multiline rad"
    ]
    lines_ex2 = normalize_raw_log_data(example_data_2)
    parsed_ex2 = parse_log_pipeline(lines_ex2)

    print("\n=== Exempel 2 ===")
    for parsed_obj in parsed_ex2:
        print(json.dumps(parsed_obj, indent=2, ensure_ascii=False, default=str))

    # EXEMPEL 3: Egen ostrukturerad text
    example_data_3 = """
    [2025-05-10 10:00:10] INFO CustomModule Startar
    Fortsättning multiline text
    { "date": "2025-05-10T10:01:00", "level": "CRITICAL", "module": "JsonModule", "msg": "Oops, ett fel!" }
    Nån okänd rad
    [2025-05-10 10:02:15] DEBUG AnotherModule Mer info
    """
    lines_ex3 = normalize_raw_log_data(example_data_3)
    parsed_ex3 = parse_log_pipeline(lines_ex3)
    sort_log_entries_by_datetime(parsed_ex3)

    print("\n=== Exempel 3 ===")
    for item in parsed_ex3:
        print(json.dumps(item, indent=2, ensure_ascii=False, default=str))

    # Om jag vill ha fler parse-funktioner:
    # parse_syslog_line, parse_custom_line, etc.
    # Och sen peta in dem i PARSER_FUNCTIONS innan multiline.


if __name__ == "__main__":
    main()
