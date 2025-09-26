# Flyway Migration Script Generator

This script automates the creation of Flyway SQL migration files within the Milk Tea E-commerce Spring Boot project. It standardizes migration file naming and template content while opening the new migration file directly in your preferred IDE (IntelliJ IDEA or VS Code).

----

## 1. Create the Script in the Project Root

Navigate to the root directory of your project and create the script file:
```bash
cd milk-tea-ecommerce-springmvc
nano create-migration.sh
```


## 2. Copy the Script Content

Paste the following bash script into `create-migration.sh`:
```bash 
#!/bin/bash
# create-migration.sh - Enhanced version

echo "🗄️  Flyway Migration Generator"
echo "================================"

# Check if migration directory exists
MIGRATION_DIR="src/main/resources/db/migration"
if [ ! -d "$MIGRATION_DIR" ]; then
    echo "❌ Migration directory not found: $MIGRATION_DIR"
    echo "Are you in the root of your Spring Boot project?"
    exit 1
fi

# Get user input
read -p "📦 Feature name (e.g. products, users): " feature
read -p "📝 Description (e.g. create_table, add_columns): " description

# Validate input
if [ -z "$feature" ] || [ -z "$description" ]; then
    echo "❌ Feature name and description are required!"
    exit 1
fi

# Generate filename
timestamp=$(date +"%Y%m%d_%H%M%S")
filename="V${timestamp}__${feature}__${description}.sql"
filepath="${MIGRATION_DIR}/${filename}"

# Create file with basic template
cat > "$filepath" << EOF
-- Migration: ${feature} - ${description}
-- Created: $(date)
-- Author: $(whoami)

-- Add your SQL statements below:

EOF

echo "✅ Created: ${filename}"
echo "📂 Location: ${filepath}"

# Open in editor (choose one)
if command -v idea &> /dev/null; then
    idea "$filepath"
    echo "🚀 Opened in IntelliJ"
elif command -v code &> /dev/null; then
    code "$filepath"
    echo "🚀 Opened in VS Code"
else
    echo "💡 Open manually: $filepath"
fi

```


## 3. Make the Script Executable

Set executable permissions to the script:
```bash
chmod +x create-migration.sh
```


## 4. Usage Examples

Run the script and follow the prompts to generate your migration files:
```bash
./create-migration.sh
```


**Example 1: Create Products Table**

- Feature name: `products`
- Description: `create_table`
- Resulting file:`V20250924_013800__products__create_table.sql`

**Example 2: Add User Authentication Columns**

- Feature name: `users`
- Description: `add_auth_columns`
- Resulting file:`V20250924_013845__users__add_auth_columns.sql`

-----
*End of migration Documentation*