import unittest
from formatting_rules import FormattingRules

class TestFormattingRules(unittest.TestCase):

    def test_class_attributes(self):
        """
        Test to ensure all class attributes are correctly defined.
        """
        self.assertEqual(FormattingRules.HEADINGS, "Use # to denote heading levels (e.g., # Heading 1, ## Heading 2, ### Heading 3, #### Heading 4).")
        self.assertEqual(FormattingRules.BOLD_TEXT, "Enclose the text within double asterisks (**text**) or double underscores (__text__).")
        self.assertEqual(FormattingRules.ITALIC_TEXT, "Enclose the text within single asterisks (*text*) or single underscores (_text_).")
        self.assertEqual(FormattingRules.STRIKETHROUGH_TEXT, "Enclose the text within double tildes (~~text~~).")
        self.assertEqual(FormattingRules.MONOSPACE_TEXT, "Enclose the text within backticks (`text`).")
        self.assertEqual(FormattingRules.BLOCKQUOTES, "Begin the line with a greater-than symbol (>).")
        self.assertEqual(FormattingRules.ORDERED_LISTS, "Start lines with numbers followed by periods (1., 2., etc.).")
        self.assertEqual(FormattingRules.UNORDERED_LISTS, "Start lines with dashes (-), plus signs (+), or asterisks (*).")
        self.assertEqual(FormattingRules.HORIZONTAL_RULES, "Use three or more hyphens (---), underscores (___), or asterisks (***) on a line by themselves.")
        self.assertEqual(FormattingRules.LINKS, "Format links as [link text](URL) (e.g., [Google](https://www.google.com)).")
        self.assertEqual(FormattingRules.IMAGES, "Embed images using ![alt text](image URL).")
        self.assertEqual(FormattingRules.CODE_BLOCKS, "Indent code by four spaces or use triple backticks (```) for code blocks.")
        self.assertEqual(FormattingRules.INLINE_CODE, "Enclose inline code within backticks (`code`).")
        self.assertEqual(FormattingRules.TABLES, "Create tables using vertical bars (|) and hyphens (-) to define the table structure.")

    def test_get_rules(self):
        """
        Test to ensure the get_rules method returns the correct dictionary.
        """
        expected_rules = {
            "headings": "Use # to denote heading levels (e.g., # Heading 1, ## Heading 2, ### Heading 3, #### Heading 4).",
            "bold_text": "Enclose the text within double asterisks (**text**) or double underscores (__text__).",
            "italic_text": "Enclose the text within single asterisks (*text*) or single underscores (_text_).",
            "strikethrough_text": "Enclose the text within double tildes (~~text~~).",
            "monospace_text": "Enclose the text within backticks (`text`).",
            "blockquotes": "Begin the line with a greater-than symbol (>).",
            "ordered_lists": "Start lines with numbers followed by periods (1., 2., etc.).",
            "unordered_lists": "Start lines with dashes (-), plus signs (+), or asterisks (*).",
            "horizontal_rules": "Use three or more hyphens (---), underscores (___), or asterisks (***) on a line by themselves.",
            "links": "Format links as [link text](URL) (e.g., [Google](https://www.google.com)).",
            "images": "Embed images using ![alt text](image URL).",
            "code_blocks": "Indent code by four spaces or use triple backticks (```) for code blocks.",
            "inline_code": "Enclose inline code within backticks (`code`).",
            "tables": "Create tables using vertical bars (|) and hyphens (-) to define the table structure."
        }
        rules = FormattingRules.get_rules()
        self.assertEqual(rules, expected_rules)
        self.assertEqual(len(rules), 13)
        for key in expected_rules:
            self.assertIn(key, rules)
            self.assertEqual(rules[key], expected_rules[key])

    # Hypotetisk testmetod om format_link-metoden finns i FormattingRules-klassen
    def test_format_link(self):
        """
        Test to ensure the format_link method returns correctly formatted links.
        """
        # Assuming the format_link method is defined in FormattingRules
        link_text = "Google"
        url = "https://www.google.com"
        expected_formatted_link = f"[{link_text}]({url})"
        actual_formatted_link = FormattingRules.format_link(link_text, url)
        self.assertEqual(actual_formatted_link, expected_formatted_link)

        link_text = "Stack Overflow"
        url = "https://stackoverflow.com"
        expected_formatted_link = f"[{link_text}]({url})"
        actual_formatted_link = FormattingRules.format_link(link_text, url)
        self.assertEqual(actual_formatted_link, expected_formatted_link)

if __name__ == '__main__':
    unittest.main()
