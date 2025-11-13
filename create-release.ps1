# GitHub Release Creation Script for AntiIdle v2.0.0
# This script creates a GitHub release with all build artifacts

$version = "2.0.0"
$tag = "v$version"
$title = "AntiIdle v$version - Modern UI & Internationalization"

Write-Host "Creating GitHub Release: $tag" -ForegroundColor Green
Write-Host "Title: $title" -ForegroundColor Cyan
Write-Host ""

# Check if authenticated
Write-Host "Checking GitHub CLI authentication..." -ForegroundColor Yellow
$authStatus = gh auth status 2>&1
if ($LASTEXITCODE -ne 0) {
    Write-Host "Not authenticated with GitHub CLI. Please run:" -ForegroundColor Red
    Write-Host "  gh auth login" -ForegroundColor Yellow
    Write-Host ""
    Write-Host "After authentication, run this script again." -ForegroundColor Yellow
    exit 1
}

Write-Host "[OK] Authenticated with GitHub CLI" -ForegroundColor Green
Write-Host ""

# Verify build artifacts exist
Write-Host "Verifying build artifacts..." -ForegroundColor Yellow
$artifacts = @(
    "build\libs\antiidle-$version-all.jar",
    "build\libs\antiidle-$version.jar",
    "build\libs\antiidle-$version-sources.jar",
    "build\libs\antiidle-$version-javadoc.jar",
    "build\distributions\antiidle-$version.zip",
    "build\distributions\antiidle-$version.tar"
)

$missing = @()
foreach ($artifact in $artifacts) {
    if (Test-Path $artifact) {
        Write-Host "  [OK] $artifact" -ForegroundColor Green
    } else {
        Write-Host "  [MISSING] $artifact" -ForegroundColor Red
        $missing += $artifact
    }
}

if ($missing.Count -gt 0) {
    Write-Host ""
    Write-Host "Missing artifacts detected. Please build the project first:" -ForegroundColor Red
    Write-Host "  .\gradlew clean build" -ForegroundColor Yellow
    exit 1
}

Write-Host ""
Write-Host "All artifacts verified!" -ForegroundColor Green
Write-Host ""

# Create the release
Write-Host "Creating GitHub release..." -ForegroundColor Yellow
Write-Host ""

gh release create $tag `
    --title $title `
    --notes-file "RELEASE_NOTES_v$version.md" `
    "build\libs\antiidle-$version-all.jar#Executable JAR (all dependencies included)" `
    "build\libs\antiidle-$version.jar#Standard JAR" `
    "build\libs\antiidle-$version-sources.jar#Source JAR" `
    "build\libs\antiidle-$version-javadoc.jar#Javadoc JAR" `
    "build\distributions\antiidle-$version.zip#Distribution ZIP" `
    "build\distributions\antiidle-$version.tar#Distribution TAR"

if ($LASTEXITCODE -eq 0) {
    Write-Host ""
    Write-Host "Success: Release created successfully!" -ForegroundColor Green
    Write-Host ""
    Write-Host "View your release at:" -ForegroundColor Cyan
    Write-Host "  https://github.com/uppnrise/antiidle/releases/tag/$tag" -ForegroundColor Yellow
} else {
    Write-Host ""
    Write-Host "Error: Failed to create release" -ForegroundColor Red
    Write-Host "Please check the error message above and try again." -ForegroundColor Yellow
    exit 1
}
