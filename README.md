# IP geolocation API

Spring Boot **4.0** sample that:

- Resolves **IP geolocation** via [IP2Location.io](https://www.ip2location.io/) using **Spring Cloud OpenFeign**
- Optionally **persists** lookups as `**Location`** rows linked to a minimal `**Country**` entity (JPA + H2)
- Optionally **restricts HTTP access by country** (default allowlist: Nigeria, Kenya, Ghana, Rwanda)

**Disclaimer:** IP2Location.io is a third-party product—check their terms, pricing, and API key requirements. This repository is **not** affiliated with IP2Location.io.

## Requirements

- **Java 17+**
- **Maven** (or use the included `./mvnw`)

## Tech stack


| Piece       | Technology                                                   |
| ----------- | ------------------------------------------------------------ |
| Runtime     | Spring Boot 4.0.4                                            |
| Web         | `spring-boot-starter-webmvc`                                 |
| HTTP client | `spring-cloud-starter-openfeign` (Spring Cloud BOM 2025.1.x) |
| Persistence | `spring-boot-starter-data-jpa`, H2 (in-memory)               |
| Tests       | `spring-boot`-starters for `webmvc-test`, `data-jpa-test`    |


## Configuration

Default port: **7020** (`server.port`).


| Key                                        | Description                                                                      |
| ------------------------------------------ | -------------------------------------------------------------------------------- |
| `ip2location.base-url`                     | Feign base URL (default `https://api.ip2location.io`)                            |
| `regional-access.enabled`                  | When `true`, most requests must come from an allowed country (by IP geolocation) |
| `regional-access.allowed-country-codes`    | ISO 3166-1 alpha-2 codes (default `NG`, `KE`, `GH`, `RW`)                        |
| `regional-access.allow-private-networks`   | When `true`, localhost / private IPs skip the geo gate (handy for dev)           |
| `regional-access.permit-all-path-prefixes` | Paths that skip the gate (default `/h2-console`, `/error`)                       |


**Reverse proxies:** Forward the real client IP (`X-Forwarded-For` or `X-Real-IP`) so the regional filter and lookups use the correct address.

**Tests:** `src/test/resources/application.yaml` sets `regional-access.enabled: false` so the suite does not require outbound geo calls.

## REST API


| Method | Path                     | Description                                                                                  |
| ------ | ------------------------ | -------------------------------------------------------------------------------------------- |
| `GET`  | `/location`              | Proxies IP2Location: omit `ip` for “current” IP, or `?ip=…`                                  |
| `GET`  | `/location?persist=true` | Same lookup; on success persists `{ "lookup", "saved" }`; **422** if payload cannot be saved |
| `POST` | `/locations`             | Body: IP2Location-style JSON → find/create `Country`, save `Location` (**201** or **400**)   |


**Persistence** requires non-blank `ip`, `country_code`, and `country_name`. Country is **find-or-create** by uppercase `country_code`.

If **regional access** is enabled and the client’s public IP resolves outside the allowlist, responses are **403** with JSON `error`, `message`, `clientIp`, and optional `resolvedCountryCode`.

## Run

```bash
cd demo   # Maven module root
./mvnw test
./mvnw spring-boot:run
```

### Example calls

```bash
# Lookup only
curl -s "http://localhost:7020/location?ip=8.8.8.8"

# Lookup + save
curl -s "http://localhost:7020/location?ip=41.90.137.253&persist=true"

# Save explicit payload
curl -s -X POST "http://localhost:7020/locations" \
  -H "Content-Type: application/json" \
  -d '{
    "ip": "41.90.137.253",
    "country_code": "KE",
    "country_name": "Kenya",
    "region_name": "Nairobi City",
    "city_name": "Nairobi",
    "latitude": -1.28333,
    "longitude": 36.81667,
    "zip_code": null,
    "time_zone": "+03:00",
    "asn": "33771"
  }'
```

H2 console (when enabled): `/h2-console` — excluded from regional checks by default.

## Project layout (main code)

```
src/main/java/com/sdl/demo/
├── DemoApplication.java          # @SpringBootApplication, @EnableFeignClients
├── access/                       # Regional gate: filter, properties, geo helper
├── country/                      # Country entity + repository
├── location/                     # Location entity, persistence, POST /locations
└── testLocation/                 # Feign client + GET /location (IpLookupController)
```

## Limitations / production notes

- **H2 in-memory** data is lost on restart; use a real database for persistence.
- **IP geolocation** is approximate (VPNs, carrier NAT, corporate networks).
- Regional enforcement is a **best-effort gate**, not a substitute for auth or legal compliance.
- Consider **caching** IP→country for the filter to reduce API traffic; add **Flyway/Liquibase**, **typed Feign DTOs**, and **resilience** (timeouts, retries) for production.

## License

No license file is included in this sample; add one if you distribute the code.