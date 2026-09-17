


BANKING PROJECT EXPLINATION OF PACKEGES


CARD PACKEGE:

File one CARDTYPE Enums: This file is used to declare the three types of cards we have the MASTER_PLATINUM,MASTER_TITANIUM,MASTER_STANDARD

with vriable of the this class being the limits that was set from the proect instructions

these varable are the paramters for our card setting the limits according to the action being taken

dailywithdrawlimit
dailytransferlimit
dailytransferownlimit
dailydepositownlimit
dailydepositlimit

a constructor is made with those limit and then we use the shortcut for creating an object in the enum class and passing the limits of each card
you can see the example in 1.0 referece in the cardtype

and then we do the get for the each one of them so that we can call the cardtype and its limits accrodingly using the getter ;
reference : 1.1

in summary the card packege is used to get the cardtype setting them accordingly and we can also from this class add new card type without changing everthing in our code keep it seperate from other for future updates

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

File Two DEBITCARD: this file will use an varable object to pass and object of cardtpye when created into the constructor of debitcard why didnt we inherete because is the card type is a type of card not the actual card
in this file we have the following consist of CARDNUMBER AND OBJECT CARDTYPE basically this card will be given a number and this number will be given to the cardtype choosen from the user when creating a new account will be more clear when we go throught the other packges

also the getter will be used to be called in other classes the connection will click evetually

in summary this class will set the cardnumber to the card type choosen


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

TRANSACTION PACKEGE

File one TRANSACTION : this file will be used to keep track of the what type of transaction happened and the account target and source account amount and  when did this transaction happen exactly

we have two constructors which will have the following

transactionId
sourceAccountId
targetAccountId
TransactionType type in here we are calling an object from the transaction type class to use to delcare what is this type of transation
amount
postBalance
timestamp

i also imported LocalDateTime and UUID will explain futher down

the first constructor will be used to create a new constuctor
the second will be used to recostruct a old record form the txt fill you see this implmentation in other packges


and for each varbale we have a get method to be used by other files

in summary : this file is our record of what has happened
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

File two Enums : this file will be used to delcare the set types of transaction

is this transaction a DEPOSITE,WITHDRAW,TRAHSFER_OWN,TRANSFER_OTHER

this is used for us not be bothered will dealing in verifiying the input is it uppercase sesitive or lower case
and in the future if we want to delcare a new type of transfer we can just add to here and add a mehtod
maybe not as simple and what i just explained but it will be easier than dealing without enums

also we have getter methods to be used when needed by other class
and the LocalDateTime is a library which is used to get the exact time of when the transaction happens

UUID is used to create a random stiring for the id of the transaction

in summary : this file is for what is the type of transaction to be used by transaction file called as a object

-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

ACCOUNT PACKEGE:

TRANSACTABLE FILE: this class is set as an interface it is used to set methods that will have to be declared in any class that will implment this class whats the use of this it is to get a place were we can set the rule for every file that inheret this

three methods are there the DEPOSITE the WITHDRAW and the TRANSFER

these will be used in the checking and saving account files and the account

question: why did the account class is abstract and why didnt we implment the withdraw will see it later in the explanation of other files

in summary : this is used to set a rules that any file that implments it has to follow
and bed there parameters

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

ACCOUNTSTATUS FILE: this consists of three things what is the account current status is it

ACTIVE DEACTIVATED,LOCKED

this is better than dealing with checking the correct input and seeing and setting .upper or lowerclass

in summary :this class is used to set the values that are going to determine is this class active or not

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

ACCOUNT File : this class will firstly import the card packege debitcard file

for what you say so that we can pass the object card as a varbale in the paramter of the account constructor

we made the class abstract why you ask to do the following this account file is to be our base for our account type to extend there

think of it do we have to write the same exact varable that is shared between each account type we can pass the commons between them as class this will make the code less and wont cause up problem down the line
and when calling the object will polymorphism it . youll see in the explnation of the main

this class has the following :

accountid

balence

DebitCard debtcard // this is the varbale called from the import of the class creditcard
so we can pass in the account object a creditcard that has been created for this user

AccountStatus status


these are the common varbale that will be in each account type created
id and balence and debt card and its status.

we created a constructor with the paramter being each of the varbales present
and setting in these the accountstatus to be active form the being why because this is when creating a new account of course the account status will be active
it wont be deactivated


THEN methods

we implemented the transactble file so we have to create the methods that were promised

deposite  it will check the amount valibaltion


transfer will have the parmater of Target account with the varbale type to be Account so we can pass that account to be sent to which will be account id correct

checking the validation for it if true withdraw from my account to the target

WAIT were is the withdraw?
think of it the checking and saving both have a withdraw methods buy each have a different way of implmenting it so whats the solution is the following
we make the class ACCOUNT TO be abstract so we dont create it yet will create those two methods in the chekcing and saving account with the super to pass the value from there to the ACCOUNT file

we have getter for each of the varable and for the status we have a setter youll ask why becuase
what will happen when this account is not active it get overdraft or locked how will change the status using this methods


in summary : this class is used to set the varbale common between each of the class type to not rewrite code again and delacre common varbale when passed to ather account type


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



CHECKING ACCOUNT:

will first import the debitcard to be used

 in this account will implment the varbalies needed for the cheking account
overdraftfee = 35.0
negativebalencewithdrawcap= 100.0
maxoverdrafthitting =2
overdraftcount

these three vrables what make the cheking account unique

then we have the constructor which consit of the accountid initalbalence,debit card

and in this constructor we have a super() this will be used to pass the value to the parent to populate to use the transfer and deposte methods if we didnt use the super
when we call the cheking account object .deposite nothing will happen because the value is not being passed to the method resposible for the mehtod implemntaiton
since this is a new account the overdraft count is zero even if we didnt add this it is zero but for extra layer of protect

this second constructor is used for account already in the databse our txt file its status will be whats in the txt and its overdraft will be whats in the txt file

to update the account accordigly this constructor is used to update a record of the user

THE WITHDRAW MEHTOD

check the amount is less than or equal zero
and if the account status is deactivated

and if the balence is <0 and the ammount is more that the cap we have set for withdraw of an negative account a messege for each senario will be shown

or deducte the amount if the balence drops below zero we add to the overdraft count and the iverdraft fee is taken also from the account

when the overdraft count is more than or ewual the maximun number of overdraft we have set it will change the status of the account ot be deactivated

so when the withdraw for the second time runs tha object which was the account cheking was created will change its status to be deactivated it will change the status in the parent class so its changes

the varbale are public we just added static and final why final so no ne can change them

second static so it common in every object created form th cheking account there wont be different value for different object of the chekign account

then we have the getter and set for the overdraft count when we want to change it either the banker or the sustem automatically when the user satisfies the condtion for removing the overdraft
and the reset

im summary : this file is used to sit the rule for overdraft and give us way to remove it once the condtion has been satisfied .and uses inheretance to pass common value to use the deposte and transfer


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



SAVINGACCOUNT:

this file has two constructor one for new account and the other for old one in the databse the same logic as the conxtucotr in the cheking account

we set the rules for saving in the withdraw

amount <=0 cant happen


first :accountId,  balance,  debitCard

with each having a super() opf course

second:  accountId,  balance,  debitCard,  status


if the account is deactivated

and if the amount is more than the accoutn balnece

its simpler than the cheking lol


-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

USER PACKEGE

ROLE file : this is an enum class meaning it is used to get the type of user who are in our system we have two CUSTOMER AND BANKER

reason the same reason we used Enums in all other packege have somthing united and when we need to add a new user we can add it from here


USER file :
this file is used to set the common varbales that all will have so we dont repeat the code we can simply pass this user blueprint into the childeren for then to us

the user file cosist of ID, NAME .PASSWORDHASH, ROLE ,FAILEDLOGINATTEMPTS,ISLOCKED

we called role class as an vrable to be used to set the type we are dealing with is it customer or Banker

when creating the id and name it is final so that it cant be used by other protected so the children will have access to it without the need of setter and getter

so will start with the constructor
we have the user constructor it will have the ID,NAME ,PASSWORDHASH AND THE ROLE

the islocked will be set to false becuase it is a brand new account so it wont be lokced

getter for the name, id ,role

getter and setter for the password what if i want to change the password

failed attempts when the banker want to reset it to zero
and setlocked to chneg it once the conditons have been met

QUESTION:

where is the rule for the login stop time and so on?

youll see it in the service folder customer the rule and the implmentation in authservice this implemntation of the login

and the failedloginattemps and is locked will be set there in the authservice


im summary this is used to set the befault info of any role in the bank


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


CUSTOMER FILE :
one new varbale is the lockeduntiltimestamp which type is long


the customer file consist of the following

first we extend User

the we create the first construcotr for a new user with the following
id name paswordhash

super() pass the following so it gets to the user

ans the lockuntiltimstamp is 0L becuse its a new account

the second constructor is used for account that alreadt exists

consist parameters are as followed : id,  name,  passwordHash,  failedLoginAttempts,  isLocked,  lockedUntilTimestamp

this is taken from the txt to check what the user status and info are

super of course and we seethe failedloginattemps and islockedd and the lockeduntiltimestamp

we then override a method we created in the user islocked

if the lockeduntimestamp is greater than 0l then we check system.mills < lockedtimstamp then we return true

the mothods for cheking that islocked and lockedforduration is not going to be impmented her this is just a blueprint they will be called and used in the quthentication service

note check the islocked when the first if doesnt work it will resutnr false form the else correct

if the currentmillis is > lockedtimstamp then thats mean the time has passed sincethe lockout

and will return false


we have a get and set of of the lockedtimestamp


///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


BANKER FILE :
the banker will only have id and name and passwordhash and the super will pass these thats it

will make hime do more things in other packges


-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

REPOSTRY PACKEGE

INTERFACES :
EACH REPOSTIRY WILL BE CONNECTED TO PACKGE THE ACCOUNT , TRANSACTION , USER

will start with the  USERREPOSITARY: this is where will declare methods that should be avilabe so when we call it and we dont care what type of database we create as long as these methods are avilable

IN THIS FILE WILL IMPRT THE USER.USER PACKEGe file

FINDALL: optinals

FINDBYNAME:String name optinals

FINDBYID:Sring id optinals

SAVE: USER user


UPDATE:User user



this will import the account account .


SECOND INTERFACE ACCOUNT :

SAVE:ACCOUNT account

FINDBYID:STRING ACCOUNT ID

FINDBYCARDNUMBER: STRING CARDNUMBER

FINDALL: OPTIONAL

UPDATE: ACCOUNT account


packege transaction transaction;


THIRD INTERFACE :


SAVE:TRANSACTION transaction

FINDBYID:STRING ACCOUNT ID

FINDBYACCOUNTIDANDDATE: STRING ACCOUNT ID, LOCALDATE DATE

FINDALL: OPTIONAL

no update becuase this should never be touched and changed oly viewed and written to



///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


FILEUSERREPOSITRY:

the methods parseuserfile is used to take a file form the folder given the path to show the detail and convert them into  there normal datatypes

formatusertoline it will be used to take the object user and convert it to a string to be inputed in another method to the file

writeusertofile: this is used to write the path of the folder where does this will go to exaclty
given the file name form user name and user id

then it will call the formatusertoline to convert it to string then it is passed to the writelines with the full path and list.of(line)





-------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

SERVICE PACKEGE :

AUTHSERVICE : this file is used to check the login and verify that such user exist in the system or not
so when a user try to login there will be mechaninsme to chck fist the user name ifit exist then it will get the password and check it by making it into a hash password and comparing it to the hash saved to this user name
if 3 attempts are counted the user is blocked for a minute then if the user doesnt exist it will show

this import the fileuserrepostriy using the methods we have created

to check by the name and then get the info needed




///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

TRASACTIONSERVICE :

this file is the brain for all money movements in our bank it handles withdraw deposite and transfer between accounts

we pass two things in the constructor
AccountRepository accountRepository
TransactionRepository transactionRepository
so we can read and update accounts and also write the transaction history records to the txt file

METHOD WITHDRAW :
first it calls getAccountOrThrow(accountId) to see if the account exist or not if not throw error
then it checks the debit card of the account if it has a card and
cardtype it calculates how much was already withdrawn today using getDailyTotal()
and checks it against getWithdrawDailyLimit() from the CardType enum
if the amount + what was already withdrawn is bigger than the limit it throws exception and stops the transaction right there
if everything is good it calls account.withdraw(amount) so the account logic handles its own balance and overdraft rules
then we update the account in accountRepository
and finally create a new Transaction object with targetAccountId as null (because money went out to cash) and save it to the transactionRepository

METHOD DEPOSITE :
it gets the account first using getAccountOrThrow
then checks the debit card limits for daily deposit using getDepositDailyLimit()
if isOwnAccount is true the limit is 200,000 if false it is 100,000
then it deposits the money using account.deposite(amount)
WAIT what if this account was deactivated because of negative balance before?
we check if the account is an instance of ChekingAccount and its status is DEACTIVATED
then we call checking.reactivate() if the balance became positive or zero it will reactivate the account automatically and print a message
then update the account in the repository and save a new Transaction record with sourceAccountId as null

METHOD TRANSFER :
first it checks if sourceAccountId is the same as targetAccountId if yes it throws error because you cant transfer to the same exact account
then gets both accounts source and target
it chooses the type if isOwnAccount is true it is TRAHSFER_OWN if not it is TRANSFER_OTHER
checks daily transfer limit on the source account card tier
if passed it withdraws from source and deposits into target
and checks if the target account was a deactivated checking account to reactivate it if balance is fixed
then updates both accounts in accountRepository
and saves the transaction with both source and target IDs

METHOD GETDAILYTOTAL :
it uses transactionRepository.findByAccountIdAndDate(accountId, LocalDate.now())
then runs a lambda stream with .filter() to match the TransactionType and sums up the total amount for today

METHOD GETFILTEREDTRANSACTIONS :
takes accountId and a filter string like "today", "yesterday", "last_7_days", "last_30_days"
uses switch case with lambda streams to filter transactions by timestamp and returns the list

in summary : this file enforces card tier daily limits coordinates balance changes between accounts and writes the records to transaction ledgers

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

BANKERSERVICE :

this file is used for banker actions and operations that normal customers are not allowed to do

it takes AccountRepository and UserRepository and TransactionRepository in its constructor

METHODS IN THIS FILE :

CREATE ACCOUNT :
the banker can create a new ChekingAccount or SavingAccount
it asks for the customer ID and initial balance and chooses the CardType (MASTER_PLATINUM, MASTER_TITANIUM, MASTER_STANDARD)
generates a card number and attaches the DebitCard to the account then calls accountRepository.save()

VIEW ALL ACCOUNTS :
calls accountRepository.findAll() so the banker can see all accounts in the system and their balances

UNLOCK CUSTOMER :
if a customer got locked out because of too many failed login attempts the banker can search for the user by ID or name
and reset failedLoginAttempts to 0 and set isLocked to false
then save the user with userRepository.update()

RESET OVERDRAFT :
if a checking account was penalized the banker can reset the overdraftcount back to 0 using the checking account method and update the file

in summary : this file gives the banker administrative power to open accounts inspect ledgers and unlock blocked users

UTIL PACKEGE

FILESTORAGEOUTILS :

this file is a helper utility class with static methods so we dont have to write try catch and file readers everywhere in our repositories

it has the following static methods :

ensureDirectoryExists(String dirPath) :
takes a folder path and checks if the folder exists if not it uses Files.createDirectories() so the program never crashes when saving to a folder that is not created yet

writeLines(String filePath, List lines) :
takes a file path and a list of strings and uses Files.write() with StandardCharsets.UTF_8
it writes the data and overwrites existing file content when updating

readLines(String filePath) :
takes a file path and uses Files.readAllLines() to return all lines as a List of Strings
if the file does not exist it catches the IOException safely and returns an empty list instead of crashing the program

in summary : this file handles raw reading and writing of txt files and folder creation in one shared place

REPOSTRY PACKEGE (CONTINUED)

FILEACCOUNTREPOSITORY :

this file implements the AccountRepository interface and handles storing account data inside text files under data/accounts folder

it has the following methods :

save(Account account) :
checks if account or accountid is null if yes throws IllegalArgumentException
then calls writeAccountToFile()

update(Account account) :
does the same null check and overwrites the existing account text file with new balance status or overdraft count

findById(String accountId) :
looks for baseDir + "/" + accountId + ".txt"
calls parseAccountFile() and wraps it inside Optional.ofNullable()

findByCardNumber(String cardNumber) :
calls findAll() and uses a lambda stream with .filter() to look through accounts that have a card and match the card number

findAll() :
looks at the baseDir folder uses a lambda (dir, name) -> name.endsWith(".txt") to grab all txt files
loops through them and parses each into an Account object and returns the list

formatAccountToLine(Account account) :
takes the account object and converts it to a single pipe delimited line
checks if it is SavingAccount or ChekingAccount to set type to "SAVINGS" or "CHECKING"
checks if debitcard is null if not extracts card number and card type name if null puts "NULL"
gets overdraft count if checking else 0
joins them with "|" : ACCOUNT_ID|BALANCE|STATUS|TYPE|CARD_NUMBER|CARD_TYPE|OVERDRAFT_COUNT

parseAccountFile(String fullPath) :
reads the file splits line by "\|"
parses accountId, balance, AccountStatus using valueOf()
reconstructs DebitCard if card number is not "NULL" using CardType.valueOf()
reconstructs ChekingAccount or SavingAccount depending on the type token

in summary : this file saves and loads account objects to and from txt files using pipe separation

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

FILETRANSACTIONREPOSITORY :

this file implements TransactionRepository and logs transactions into two places :
a global ledger file and individual account ledger files

save(Transaction transaction) :
converts the transaction into a pipe delimited line :
transactionId|sourceAccountId|targetAccountId|type|amount|postBalance|timestamp

appends the line to the master file data/transactions/all_transactions.txt

if sourceAccountId is not null it appends to data/transactions/Transactions-[sourceAccountId].txt

if targetAccountId is not null and not equal to sourceAccountId it appends to data/transactions/Transactions-[targetAccountId].txt

findByAccountId(String accountId) :
reads data/transactions/Transactions-[accountId].txt
splits each line and parses the tokens back into Transaction objects using the second constructor

findByAccountIdAndDate(String accountId, LocalDate date) :
calls findByAccountId(accountId) and uses a lambda stream .filter() to keep only transactions where timestamp.toLocalDate().isEqual(date)

appendLine(String filePath, String line) :
checks if parent directory exists if not creates it
uses Files.write with StandardOpenOption.CREATE and StandardOpenOption.APPEND so it adds the new line to the bottom without erasing old lines

in summary : this file provides an immutable append only log of all banking actions for audits and statements

MAIN (APPLICATION ENTRY POINT)

MAIN FILE :

this is where the whole program starts with public static void main(String[] args)

it initializes all the repositories first :
FileUserRepository, FileAccountRepository, FileTransactionRepository

then initializes the services passing the repositories to them :
AuthService, TransactionService, BankerService

it runs a while(true) loop showing the login screen :
asks for username and password
passes them to authService.login()
if login fails 3 times it tells the user they are locked out for 60 seconds

if login succeeds it checks user.getRole() :

IF ROLE IS CUSTOMER :
shows customer menu :

View Balance and Card Details

Deposit Money

Withdraw Money (checks limits against their CardType)

Transfer Money (checks own vs other account limits)

View Transaction History (with filters: today, 7 days, 30 days)

Logout

IF ROLE IS BANKER :
shows banker menu :

Create New Account and Issue DebitCard

View All Customer Accounts

Unlock Locked Customer Account

Reset Overdraft Limit / Counter

View Global Transaction Audit Log

Logout

in summary : this file connects all packages together handles scanner inputs and directs the user to either customer or banker menus based on authentication