                                                # Tjänst

Auth-ServerProject

## Om tjänsten

Auth-Server ansvarar för att skapa ny användare och spara användaren till databasen, när användaren är registrerad kan
AuthServer
hämta användaren från databasen för att logga in i applikationen samt skapa JWT-token.

## Teknik

- Java 24
- Spring Security
- SpringBoot
- Spring Data JPA
- MapStruct
- H2 (för att spara i databas vid lokal körning)
- Lombok
- PostgreSQL (för långsiktig sparning av användare)
- Maven

## Köra lokalt

AuthServerProject är en SpringBootApplikation och skapar en egen main klass. Main klassen heter
AuthServerProjectApplication och behövs för att köra projektet. I main klassen klickar du på pilen vid "public class
AuthServerProjectApplication" för att starta hela applikationen.

### Krav

För att köra applikationen behöver SpringBoot vara installerat, i pom.xml kan du gå in i "AddStarters", om något saknas
kan du klicka i det för att lägga in i pom.xml och därmed i projektet.
Under DevTools behöver "SpringBoot DevTools"
och "Lombok" vara bockade.
Under Web -> Spring Web och SpringDoc OpenAPI.
Security -> Spring Security och OAuth2
Resource Server.
SQL -> JDBC API, Spring Data JPA, H2Database, PostgreSQL Driver.
I/O -> Validation
Ops -> CycloneDX SBOM support

### Miljövariabler

Lista miljövariabler som behövs, utan riktiga lösenord, tokens eller nycklar.

DB_USERNAME = Användarnamn till PostgreSQL databasen som används för produktion
DB_PASSWORD = Lösenord till PostgreSQL databasen som används för produktion
DB_URL = URL till PostgreSQL databasen som används för produktion
JWT_PRIVATE_KEY (Fås när man startar main metoden i klassen "AuthServerProjectApplication", hittas i terminalen)
JWT_PUBLIC_KEY (Fås när man startar main metoden i klassen "AuthServerProjectApplication", hittas i terminalen)

För att få nycklarna behöver man avkommentera koden, nycklarna kommer att visas i terminalen där man kopierar de och
lägger in bland miljövariabler. När man fått nycklarna kan kodens kommenteras bort igen.

### Starta tjänsten

AuthServerProject körs på porten http://localhost:8080

## API / Endpoints

I AuthServer finns endpoint för att skapa användare, logga in, hämta JWT-token.

AppUserController:

- RequestMapping /users
- POST /register - registrera användare
- GET /users - hämtar alla användare

AuthController:

- RequestMapping /auth
- POST /login - loggar in användare
- GET /jwks - hämtar jwks nyckel kopplad användaren
- GET /me - återställer användare vid omladdning av sidan

## Övrigt

För att testa Auth-server lokalt kan man gå till http://localhost:8080/swagger-ui/index.html efter att ha startat
applikationen.
För att testa driftsatta versionen av Auth-server kan man gå
till https://auth-serverproject-1-0-0.onrender.com/swagger-ui/index.html
Vill du testa hela flödet lokalt ska du även ha backend servicena order-service, product-service, jwt-gateway och
email-service. Du behöver även ha frontend projektet som heter Frontend.
För lokal körning behöver de startas i denna ordning:

- Auth-serverProject
- Product-service
- Order-service
- Email-service
- jwt-gateway
- Frontend