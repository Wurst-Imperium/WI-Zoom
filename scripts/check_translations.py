import util
from pathlib import Path

translations_dir = Path("src") / "main" / "resources" / "assets" / "wi_zoom" / "lang"


def show_translation_stats(en_us: dict, translations: dict):
	"""Render a table of the current translation progress for each language."""
	util.add_github_summary("| Language | Translated | % |")
	util.add_github_summary("| --- | --- | --- |")
	util.add_github_summary(f"| en_us | {len(en_us)} | 100.00% |")
	for lang, data in translations.items():
		util.add_github_summary(f"| {lang} | {len(data)} | {len(data) / len(en_us) * 100:.2f}% |")
	util.add_github_summary("")


def check_extra_keys(en_us: dict, translations: dict):
	"""Check if any translation files contain keys that don't exist in the original."""
	extra_keys_found = False
	for lang, data in translations.items():
		extra_keys = set(data.keys()) - set(en_us.keys())
		if extra_keys:
			extra_keys_found = True
			util.add_github_summary(
				f"⚠ {lang}.json contains translations that don't exist in en_us.json ({len(extra_keys)} found):"
			)
			for key in extra_keys:
				util.add_github_summary(f"- {key}")
	if extra_keys_found:
		raise Exception("Found extra keys in one or more translation files, see summary")
	util.add_github_summary("✅ No extra keys found")


def check_untranslated_strings(en_us: dict, translations: dict):
	"""Check if any translation files contain untranslated strings."""
	untranslated_strings_found = False
	intentionally_untranslated = util.read_json_file(
		Path("src") / "main" / "resources" / "intentionally_untranslated.json"
	)

	for lang, data in translations.items():
		untranslated_strings = set()
		for key, value in data.items():
			if value == en_us[key]:
				if lang in intentionally_untranslated and key in intentionally_untranslated[lang]:
					continue
				untranslated_strings.add(key)
		if untranslated_strings:
			untranslated_strings_found = True
			util.add_github_summary(
				f"⚠ {lang}.json contains strings that are identical to en_us.json ({len(untranslated_strings)} found):"
			)
			for key in untranslated_strings:
				util.add_github_summary(f"- {key}: {en_us[key]}")
			util.add_github_summary(
				"\nIf this is intentional, add the affected key(s) to intentionally_untranslated.json:"
			)
			util.add_github_summary("```json")
			util.add_github_summary(f'  "{lang}": [')
			for key in untranslated_strings:
				util.add_github_summary(f'    "{key}"')
			util.add_github_summary("  ]")
			util.add_github_summary("```")

	if untranslated_strings_found:
		raise Exception("Found untranslated strings in one or more translation files, see summary")
	util.add_github_summary("✅ No accidentally untranslated strings found")


def check_order_of_strings(en_us: dict, translations: dict):
	"""Check if the strings in each translation file are in the same order as in en_us.json."""
	for lang, data in translations.items():
		en_us_keys_present_in_translation = [key for key in en_us.keys() if key in data.keys()]
		translation_keys_present_in_en_us = [key for key in data.keys() if key in en_us.keys()]
		if en_us_keys_present_in_translation != translation_keys_present_in_en_us:
			raise Exception(f"⚠ The order of strings in {lang}.json is different from en_us.json")
	util.add_github_summary("✅ The order of strings in each translation file matches en_us.json")


def main():
	en_us = util.read_json_file(translations_dir / "en_us.json")
	translations = {}
	for path in sorted(translations_dir.rglob("*.json"), key=lambda x: x.name):
		if path.is_file() and path.name != "en_us.json":
			lang = path.name.removesuffix(".json")
			data = util.read_json_file(path)
			translations[lang] = data

	show_translation_stats(en_us, translations)
	check_extra_keys(en_us, translations)
	check_untranslated_strings(en_us, translations)
	check_order_of_strings(en_us, translations)


if __name__ == "__main__":
	main()
