# PleaseNoteMe

My first app in Android. Add notes, edit them and delete them. It also uses a fake web service to search for a word in a thesaurus
This is the original requirement:

Notes for new android app
Abstract: An user wants to create notes to write out his thoughts. If the user wants to know the meaning of a word he can ask to a service.

Types of users:
Common user of a smart phone running android 4.4 kit-kat at least, who is using internet to connect web services

Glossary
User: The only type of user in the app. He wants to write out his thoughts in his smart phone
Smart Phone: Communication device running Android Operative System version KitKat
Note: A small text to store in the device. A title is included to help sort them. The notes can be stored, updated and removed from the device


Functional Requirements
Create a note with a limit of characters at the title and in the content. The notes should be stored locally (SQLite). All the notes can be updated or deleted
The notes must be sorted alphabetically by title
When a word is double tapped it call a dictionary service (via web services)
A dictionary web service must be created. Use a little base knowledge to show the result. No security validation by the moment. Web service will use JSON to communicate
If web service connection fails, the dialog box should say so instead of just crash
The app should be available in the google app store
The app should be running in a actual  smart phone
Look and feel will be graded as part of the functionality. Screen Orientation should fit in the page properly
(Extra) Integrate Google Wallet: Android In-app Billing SDK to the app to buy for free, the premium service with longer definitions

Non-functional requirements
Project must be built following the principles of agile and Test Driven Development


Scenarios 
Download the app from the google app store and install it
Create a shortcut in the phone

Open the app
Tap on the “New” button. The editor opens
Write a title. Write some text until the limit of characters came in. A message will let you know your message is to long to store it
Tap on finish. The message will be stored
The message will be added in the list at the left, sorted alphabetically by title

Open the app
Tap over the title of a previously created note. The editor opens
Long tap over a word will open the dictionary (via web services). If the word is found, the meaning appears in a dialog window. If the word does not appear then the modal window will say “not found”.
Overwrite the content of the note
Check up the message was updated properly


I added more specifications:
Opoen the app. See a list view of notes and a "Note Me" button to create a new note

If I tap over a note, the edition scrreen appears with the original note in it. The note is editable at this point
    I can come back saving the untouched content or update title/content then tap on Save button. 
    If I change it and tap over Back button a Toast appears with legend “It was saved automatically”
    In this same screen theres a button "Delete" right next to Save button. Tapping on this button, an alert dialog appears with 
    a confirmation message. If  I press Cancel, nothing happens. Other wise it will return me to the main screen and a Toast telling me 
    the status of the removal.
    If I press over a word for a while the meaning of this word appears on a dialog message

