import argparse

if __name__ == "__main__":
	parser = argparse.ArgumentParser()
	parser.add_argument("mc_version", help="Minecraft version")
	parser.add_argument("yarn_mappings", help="Yarn mappings version")
	parser.add_argument("fabric_loader", help="Fabric Loader version")
	parser.add_argument("fapi_version", help="Fabric API version")
	args = parser.parse_args()

	# Read gradle.properties
	print("Updating gradle.properties...")
	with open("gradle.properties", "r") as f:
		lines = f.readlines()

	# Define replacements
	replacements = {
		"minecraft_version": lambda v: args.mc_version,
		"yarn_mappings": lambda v: args.yarn_mappings,
		"loader_version": lambda v: args.fabric_loader,
		"fabric_version": lambda v: args.fapi_version,
		"mod_version": lambda v: v[: v.index("MC") + 2] + args.mc_version,
	}

	# Update lines
	for i, line in enumerate(lines):
		if line.startswith("#"):
			continue
		parts = line.split("=")
		if len(parts) != 2:
			continue
		key = parts[0]
		if key.strip() not in replacements:
			continue
		old_value = parts[1]
		new_value = replacements[key.strip()](old_value)
		print(f"{key}={old_value} -> {new_value}")
		lines[i] = f"{key}={new_value}\n"

	# Save modified gradle.properties
	with open("gradle.properties", "w") as f:
		f.writelines(lines)
	print("gradle.properties updated.")
