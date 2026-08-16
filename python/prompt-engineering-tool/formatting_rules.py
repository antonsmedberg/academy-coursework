# formatting_rules.py

class FormattingRules:
    HEADINGS = "Use # to denote heading levels (e.g., # Heading 1, ## Heading 2, ### Heading 3, #### Heading 4)."
    BOLD_TEXT = "Enclose the text within double asterisks (**text**) or double underscores (__text__)."
    ITALIC_TEXT = "Enclose the text within single asterisks (*text*) or single underscores (_text_)."
    STRIKETHROUGH_TEXT = "Enclose the text within double tildes (~~text~~)."
    MONOSPACE_TEXT = "Enclose the text within backticks (`text`)."
    BLOCKQUOTES = "Begin the line with a greater-than symbol (>)."
    ORDERED_LISTS = "Start lines with numbers followed by periods (1., 2., etc.)."
    UNORDERED_LISTS = "Start lines with dashes (-), plus signs (+), or asterisks (*)."
    HORIZONTAL_RULES = "Use three or more hyphens (---), underscores (___), or asterisks (***) on a line by themselves."
    LINKS = "Format links as [link text](URL) (e.g., [Google](https://www.google.com))."
    IMAGES = "Embed images using ![alt text](image URL)."
    CODE_BLOCKS = "Indent code by four spaces or use triple backticks (```) for code blocks."
    INLINE_CODE = "Enclose inline code within backticks (`code`)."
    TABLES = "Create tables using vertical bars (|) and hyphens (-) to define the table structure."

    @classmethod
    def get_rules(cls) -> dict:
        """
        Returns all formatting rules as a dictionary.

        Returns:
            dict: A dictionary of formatting rules with rule names as keys and their descriptions as values.
        """
        return {
            "headings": cls.HEADINGS,
            "bold_text": cls.BOLD_TEXT,
            "italic_text": cls.ITALIC_TEXT,
            "strikethrough_text": cls.STRIKETHROUGH_TEXT,
            "monospace_text": cls.MONOSPACE_TEXT,
            "blockquotes": cls.BLOCKQUOTES,
            "ordered_lists": cls.ORDERED_LISTS,
            "unordered_lists": cls.UNORDERED_LISTS,
            "horizontal_rules": cls.HORIZONTAL_RULES,
            "links": cls.LINKS,
            "images": cls.IMAGES,
            "code_blocks": cls.CODE_BLOCKS,
            "inline_code": cls.INLINE_CODE,
            "tables": cls.TABLES
        }



