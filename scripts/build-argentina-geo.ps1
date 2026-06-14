# Script de un solo uso: genera src/main/resources/data/argentina-geo.csv
# (formato simple "Provincia;Ciudad" por linea) a partir del dataset abierto
# dr5hn/countries-states-cities-database (MIT).
$dir = "C:\Users\Iara Blandi\AppData\Local\Temp\geo"

$cities = [System.IO.File]::ReadAllText("$dir\AR.json", [System.Text.Encoding]::UTF8) | ConvertFrom-Json
$states = [System.IO.File]::ReadAllText("$dir\states.json", [System.Text.Encoding]::UTF8) | ConvertFrom-Json

$arStates = $states | Where-Object { $_.country_code -eq "AR" }

$nombrePorId = @{}
foreach ($s in $arStates) {
    $nombre = $s.name
    if ($nombre -eq "Autonomous City of Buenos Aires") {
        $o = [char]0x00F3
        $nombre = "Ciudad Aut" + $o + "noma de Buenos Aires"
    }
    $nombrePorId[$s.id] = $nombre
}

$porProvincia = @{}
foreach ($c in $cities) {
    $prov = $nombrePorId[$c.state_id]
    if (-not $porProvincia.ContainsKey($prov)) {
        $porProvincia[$prov] = New-Object System.Collections.Generic.List[string]
    }
    $porProvincia[$prov].Add($c.name)
}

# El dataset no incluye "Mar del Plata" por su nombre habitual (queda como
# "General Pueyrredon", el partido). Se agrega a mano por ser clave para envios.
$porProvincia["Buenos Aires"].Add("Mar del Plata")

$lineas = New-Object System.Collections.Generic.List[string]
foreach ($prov in ($porProvincia.Keys | Sort-Object)) {
    foreach ($ciudad in ($porProvincia[$prov] | Sort-Object -Unique)) {
        $lineas.Add("$prov;$ciudad")
    }
}

Write-Output ("Provincias: " + $porProvincia.Count)
Write-Output ("Lineas totales: " + $lineas.Count)

$outPath = "src\main\resources\data\argentina-geo.csv"
[System.IO.File]::WriteAllLines($outPath, $lineas, [System.Text.UTF8Encoding]::new($false))
Write-Output "Escrito: $outPath"
