Feature: Mailchimp

  Scenario Outline: Create accounts
    Given i have chosen <browser>
    Given i have entered a email address "<email>"
    Given i have entered a username "<username>"
    Given i have entered a password "<password>"
    When i click the sign up button
    Then i create an account "<result>"
    Examples:
      | browser | email  | username | password     | result       |
      | chrome  | Random | Random   | Password123! | Pass         |
      | chrome  | Random | TooLong  | Password123! | CharLimit    |
      | chrome  | Random | Used     | Password123! | AlreadyInUse |
      | chrome  | Blank  | Random   | Password123! | BlankEmail   |
      | edge    | Random | Random   | Password123! | Pass         |
      | edge    | Random | TooLong  | Password123! | CharLimit    |
      | edge    | Random | Used     | Password123! | AlreadyInUse |
      | edge    | Blank  | Random   | Password123! | BlankEmail   |
## Input Beskrivning
## Blank skickar en tom input
## Random på både email och username skapar en slumpad input (email lägger till "@gmail.com" på slutet)
## TooLong skapar en slumpad input på 105 karaktärer
## Used gör att username input blir "Username" då den är upptagen sen tidigare
##
## Result Beskrivning
## Pass innebär att kontot skapades utan problem
## CharLimit innebär att användarnamnet var för långt (över 100 karaktärer)
## AlreadyInUse innebär att användarnamnet redan är under användning
## BlankEmail innebär att email fältet var tomt eller saknade ett @