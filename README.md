# nodep Android

## Как у конкурентов, но без конфликта VPN (РФ)

| | BetBlocker / OFFBET | nodep |
|--|--|--|
| Локальный VPN (DNS) | да | нет (мешает VPN в РФ) |
| Accessibility | да | **да — основной слой** |
| Работает с обычным VPN | часто нет | **да** |

Блокирует:
- приложения БК/казино по package name
- сайты в браузере по URL / тексту на экране (бренды + blocklist)

## Сборка APK

1. Android Studio → Open → эта папка
2. Build → Build APK(s)
3. `app/build/outputs/apk/debug/app-debug.apk`

## Включение

1. Открыть nodep → «включить защиту»
2. В спец. возможностях включить **nodep**
3. Свой VPN для интернета можно оставить включённым
