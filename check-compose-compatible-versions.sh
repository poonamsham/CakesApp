#!/usr/bin/env bash
set -euo pipefail

# check-compose-versions.sh
#
# Run from your project root:
#   chmod +x scripts/check-compose-versions.sh
#   ./scripts/check-compose-versions.sh

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
cd "$ROOT_DIR"

command -v curl >/dev/null 2>&1 || {
  echo "ERROR: curl is required."
  exit 1
}

latest_stable_from_metadata() {
  local url="$1"
  curl -fsSL "$url" \
    | grep -oE '<version>[^<]+</version>' \
    | sed -E 's#</?version>##g' \
    | grep -E '^[0-9]+([.][0-9]+)*$' \
    | tail -1
}

find_version_catalog_value() {
  local key="$1"
  local file="gradle/libs.versions.toml"

  if [[ -f "$file" ]]; then
    awk -F'=' -v key="$key" '
      $1 ~ "^[[:space:]]*" key "[[:space:]]*$" {
        gsub(/[[:space:]"]/, "", $2)
        print $2
        exit
      }
    ' "$file"
  fi
}

detect_plugin_version() {
  local plugin_id="$1"
  local value=""

  case "$plugin_id" in
    "com.android.application")
      value="$(find_version_catalog_value "agp" || true)"
      ;;
    "org.jetbrains.kotlin.android")
      value="$(find_version_catalog_value "kotlin" || true)"
      ;;
    "org.jetbrains.kotlinx.kover")
      value="$(find_version_catalog_value "kover" || true)"
      ;;
  esac

  if [[ -n "${value:-}" ]]; then
    echo "$value"
    return
  fi

  for file in build.gradle.kts build.gradle; do
    [[ -f "$file" ]] || continue

    value="$(
      grep -E "id[[:space:]]*\\(?[\"']${plugin_id//./\\.}[\"']\\)?[[:space:]]*version" "$file" 2>/dev/null \
        | sed -E "s/.*version[[:space:]]*[= ]*[\"']([^\"']+)[\"'].*/\\1/" \
        | head -1 || true
    )"

    if [[ -n "${value:-}" ]]; then
      echo "$value"
      return
    fi
  done
}

detect_gradle_wrapper() {
  local file="gradle/wrapper/gradle-wrapper.properties"

  if [[ -f "$file" ]]; then
    grep '^distributionUrl=' "$file" \
      | sed -E 's#.*gradle-([0-9][0-9.]+)-(bin|all)\\.zip#\\1#' \
      | head -1
  fi
}

minimum_gradle_for_agp() {
  local agp="$1"
  local major minor
  major="$(echo "$agp" | cut -d. -f1)"
  minor="$(echo "$agp" | cut -d. -f2)"

  case "${major}.${minor}" in
    9.4) echo "9.6.0" ;;
    9.3) echo "9.5.0" ;;
    9.2) echo "9.4.1" ;;
    9.1) echo "9.3.1" ;;
    9.0) echo "9.1.0" ;;
    8.13) echo "8.13" ;;
    8.12|8.11) echo "8.13" ;;
    8.10|8.9) echo "8.11.1" ;;
    8.8) echo "8.10.2" ;;
    8.7) echo "8.9" ;;
    8.6|8.5) echo "8.7" ;;
    8.4) echo "8.6" ;;
    8.3) echo "8.4" ;;
    8.2) echo "8.2" ;;
    8.1|8.0) echo "8.0" ;;
    7.4) echo "7.5" ;;
    7.3) echo "7.4" ;;
    7.2) echo "7.3.3" ;;
    7.1) echo "7.2" ;;
    7.0) echo "7.0" ;;
    *) echo "unknown" ;;
  esac
}

version_ge() {
  [[ "$(printf '%s\n%s\n' "$2" "$1" | sort -V | tail -1)" == "$1" ]]
}

AGP_CURRENT="$(detect_plugin_version "com.android.application" || true)"
KOTLIN_CURRENT="$(detect_plugin_version "org.jetbrains.kotlin.android" || true)"
KOVER_CURRENT="$(detect_plugin_version "org.jetbrains.kotlinx.kover" || true)"
GRADLE_CURRENT="$(detect_gradle_wrapper || true)"

echo "=============================================="
echo " Jetpack Compose compatibility/version check"
echo "=============================================="
echo
echo "Current project:"
printf "  AGP:            %s\n" "${AGP_CURRENT:-not detected}"
printf "  Kotlin:         %s\n" "${KOTLIN_CURRENT:-not detected}"
printf "  Gradle wrapper: %s\n" "${GRADLE_CURRENT:-not detected}"
printf "  Kover:          %s\n" "${KOVER_CURRENT:-not detected}"
echo

if [[ -n "${AGP_CURRENT:-}" ]]; then
  MIN_GRADLE="$(minimum_gradle_for_agp "$AGP_CURRENT")"
  echo "Compatibility:"
  printf "  AGP %s requires Gradle >= %s\n" "$AGP_CURRENT" "$MIN_GRADLE"

  if [[ "$MIN_GRADLE" != "unknown" && -n "${GRADLE_CURRENT:-}" ]]; then
    if version_ge "$GRADLE_CURRENT" "$MIN_GRADLE"; then
      echo "  Gradle/AGP:     OK"
    else
      echo "  Gradle/AGP:     INCOMPATIBLE"
      echo "  Suggested fix:  ./gradlew wrapper --gradle-version $MIN_GRADLE"
    fi
  fi
  echo
fi

if [[ -n "${KOTLIN_CURRENT:-}" ]]; then
  KOTLIN_MAJOR="$(echo "$KOTLIN_CURRENT" | cut -d. -f1)"
  if [[ "$KOTLIN_MAJOR" -ge 2 ]] 2>/dev/null; then
    echo "Compose compiler:"
    echo "  Kotlin 2.x detected."
    echo "  Use org.jetbrains.kotlin.plugin.compose with the SAME version as Kotlin:"
    echo "      id(\"org.jetbrains.kotlin.plugin.compose\") version \"$KOTLIN_CURRENT\""
    echo
  else
    echo "Compose compiler:"
    echo "  Kotlin < 2.0 detected."
    echo "  Check Google's Compose-to-Kotlin compatibility map."
    echo
  fi
fi

echo "Fetching latest stable versions..."
echo

AGP_LATEST="$(latest_stable_from_metadata   "https://dl.google.com/dl/android/maven2/com/android/tools/build/gradle/maven-metadata.xml" || true)"

KOTLIN_LATEST="$(latest_stable_from_metadata   "https://repo1.maven.org/maven2/org/jetbrains/kotlin/kotlin-gradle-plugin/maven-metadata.xml" || true)"

COMPOSE_BOM_LATEST="$(latest_stable_from_metadata   "https://dl.google.com/dl/android/maven2/androidx/compose/compose-bom/maven-metadata.xml" || true)"

KOVER_LATEST="$(latest_stable_from_metadata   "https://repo1.maven.org/maven2/org/jetbrains/kotlinx/kover-gradle-plugin/maven-metadata.xml" || true)"

printf "  Latest stable AGP:         %s\n" "${AGP_LATEST:-unavailable}"
printf "  Latest stable Kotlin:      %s\n" "${KOTLIN_LATEST:-unavailable}"
printf "  Latest stable Compose BOM: %s\n" "${COMPOSE_BOM_LATEST:-unavailable}"
printf "  Latest stable Kover:       %s\n" "${KOVER_LATEST:-unavailable}"

echo
echo "After changing versions, validate with:"
echo "  ./gradlew clean build test lint"
