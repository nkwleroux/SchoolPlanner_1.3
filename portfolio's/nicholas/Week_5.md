# Week 5

##	Wat is de situatie (context)?

In dit week heb ik voor 25 uren gewerkt aan de project en heb voor elke werkdag in de week aan de project gezeten. In die tijd had ik fuctionaliteit toegevoegd bij de instellingen: subject, room, group, student, teacher. Had een hulpklasse RoomData gemaakt maar had die later verwijderd. Dit week was er problemen met de instelling gui van docent. Er was een probleem met de onderwerpen opslaan en aanpassen. Ik kon het niet fixen en had het voor de volgende week gelaten.

##	Welke keuzemogelijkheden heb je?

Voor subject, room, group, student, en teacher had ik met Tom kunnen beslissen de structuur van het beeld. Ik kon met Tom kiezen wat de vier instellingen zou doen qua fuctionaliteit. 

##	Welke keuze heb je gemaakt?

Voor subject en group hadden wij gekozen om een ListView te gebruiken. Voor room, student, en teacher was het meer ingewikkeld. Ik had voor een tableview gekozen. In het begin had ik een hulpklasse gemaakt RoomData om de data van room op te slaan voor de tableview. Ik had later in de week de klasse verwijderd en het toegevoegd aan SimpleConverter. Ik had ook op dezelfde de naam van SimpleConverter naar SimplePropertyConverter veranderd.  

##	Waarom heb je deze keuze gemaakt?

We hadden een Listview gekozen want er was alleen de naam van de groep en subject die we moesten afbeelden. 
We hadden een Tableview gekozen want bij room moesten we afbeelden de kamer naam, capaciteit en de kamer type. Voor de student en teacher moest ik de namen, id, voor studenten de groep en voor de docenten de onderwerpen die ze geven afbeelden. 
De reden waarom ik SimpleConverter naar SimplePropertyConverter had veranderd was omdat er veel tableviews gebruik zou gebruik van maken. Voorbeelden zijn de klasse contacts list die docenten en studenten afbeelden, er wordt dezelfde gedaan voor kamers, studenten, docenten in instellingen gedaan.