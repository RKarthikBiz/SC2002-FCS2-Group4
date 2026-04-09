param(
    [switch]$NoClean,
    [Parameter(ValueFromRemainingArguments = $true)]
    [string[]]$JavaArgs
)

$ErrorActionPreference = 'Stop'
$repoRoot = Split-Path -Parent $MyInvocation.MyCommand.Path

Push-Location $repoRoot
try {
    if (-not $NoClean -and (Test-Path out)) {
        Remove-Item -Recurse -Force out
    }

    if (-not (Test-Path out)) {
        New-Item -ItemType Directory -Path out | Out-Null
    }

    $sources = Get-ChildItem -Recurse -Path "src/main/java" -Filter "*.java" | ForEach-Object { $_.FullName }
    if (-not $sources -or $sources.Count -eq 0) {
        Write-Error "No Java source files found under src/main/java."
        exit 1
    }

    javac -d out $sources
    if ($LASTEXITCODE -ne 0) {
        exit $LASTEXITCODE
    }

    java -cp out Main @JavaArgs
    exit $LASTEXITCODE
}
finally {
    Pop-Location
}
