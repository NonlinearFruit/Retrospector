String hint = "Give it time";
apprentice = new Achievement("","Apprentice","Retrospect for 1 year",3);
apprentice.setHint(hint);
journeyman = new Achievement("","Journeyman","Retrospect for 5 years",2);
journeyman.setHint(hint);
master = new Achievement("","Master","Retrospect for 10 years",1);

String hint = "The weakest link";
researcher = new Achievement("","Researcher","1k facts for each factoid",1);
researcher.setHint(hint);
scientist = new Achievement("","Scientist","100 facts for each factoid",2);
scientist.setHint(hint);
objective = new Achievement("","Objective","10 facts for each factoid",3);
objective.setHint(hint);

hint = "One factoid to rule them all";
enigmatologist = new Achievement("","Enigmatologist","Factoid with 10k facts",1);
enigmatologist.setHint(hint);
riddler = new Achievement("","Riddler","Factoid with 1k facts",2);
riddler.setHint(hint);
puzzler = new Achievement("","Puzzler","Factoid with 100 facts",3);
puzzler.setHint(hint);

hint = "The pursuit of knowledge";
expert = new Achievement("","Trivia Expert","Collect 10k factoids",1);
expert.setHint(hint);
pro = new Achievement("","Trivia Pro","Collect 1k factoids",2);
pro.setHint(hint);
whiz = new Achievement("","Trivia Whiz","Collect 100 factoids",3);
whiz.setHint(hint);

hint = "The more, the merrier";
trivial = new Achievement("","Trivial Pursuit","Have 5 factoid types",3);

paranoid = new Achievement("","Paranoia","Keep a backup",2);
paranoid.setHint("Be prepared");

inspector = new Achievement("","Inspector","Edit the .config file",2);
inspector.setHint("Customize");

stargazer = new Achievement("","Star Gazer","Star Retrospector",1);
stargazer.setHint("Show some love");

stargazerAndroid = new Achievement("","Droid Dreamer","Star the App",1);
stargazerAndroid.setHint("Show some love");

String hint = "Master of none";
renaissance = new Achievement("","Renaissance Man","1k media in each category",1);
renaissance.setHint(hint);
rounded = new Achievement("","Well Rounded","100 media in each category",2);
rounded.setHint(hint);
jack = new Achievement("","Jack of All Trades","10 media in each category",3);
jack.setHint(hint);

hint = "Specialize";
academic = new Achievement("","Academic","Category with 10k media",1);
academic.setHint(hint);
enthusiast = new Achievement("","Enthusiast","Category with 1k media",2);
enthusiast.setHint(hint);
dabbler = new Achievement("","Dabbler","Category with 100 media",3);
dabbler.setHint(hint);

hint = "Gotta catch them all";
connoisseur = new Achievement("","Connoisseur","Collect 10k media",1);
connoisseur.setHint(hint);
collector = new Achievement("","Collector","Collect 1k media",2);
collector.setHint(hint);
hobbiest = new Achievement("","Hobbyist","Collect 100 media",3);
hobbiest.setHint(hint);

spectrum = new Achievement("","Spectrum","Have more categories than colors",1);
spectrum.setShowable(false);

hint = "Scattergories";
diversify = new Achievement("","Diversify","Have 5 categories",3);

spree = new Achievement("", "Spree", "5 categories in a day", 1);
spree.setShowable(false);
binge = new Achievement("", "Binge", "10 media in 1 category in a day", 1);
binge.setShowable(false);
marathon = new Achievement("", "Marathon", "5 users review 5 media in a day", 1);
marathon.setShowable(false);

hint = "Top the charts";
topScoreGold = new Achievement("", "Hardcore Gamer", "High score over 120", 1);
topScoreGold.setHint(hint);
topScoreSilver = new Achievement("", "Die-Hard Fan", "High score over 90", 2);
topScoreSilver.setHint(hint);
topScoreBronze = new Achievement("", "Peerless", "High score over 60", 3);
topScoreBronze.setHint(hint);

hint = "Don't accept 2nd place";
lowScoreGold = new Achievement("", "Valedictorian", "All high scores over 30", 1);
lowScoreGold.setHint(hint);
lowScoreSilver = new Achievement("", "Salutatorian", "All high scores over 20", 2);
lowScoreSilver.setHint(hint);
lowScoreBronze = new Achievement("", "Top Marks", "All high scores over 10", 3);
lowScoreBronze.setHint(hint);

hint = "Everyday same old same old";
topStreakGold = new Achievement("", "Rampage", "Media streak over 28", 1);
topStreakGold.setHint(hint);
topStreakSilver = new Achievement("", "Frenzy", "Media streak over 21", 2);
topStreakSilver.setHint(hint);
topStreakBronze = new Achievement("", "Spree", "Media streak over 14", 3);
topStreakBronze.setHint(hint);

hint = "No streak gets left behind!";
lowStreakGold = new Achievement("", "Fast Forward", "All media streaks over 14", 1);
lowStreakGold.setHint(hint);
lowStreakSilver = new Achievement("", "Double Time", "All media streaks over 7", 2);
lowStreakSilver.setHint(hint);
lowStreakBronze = new Achievement("", "Triplets", "All media streaks over 3", 3);

starwars = new Achievement("","Star Wars","Star Wars in a title",3);
starwars.setShowable(false);
nerd = new Achievement("","Nerd","Media with 10 facts",3);
nerd.setShowable(false);

inconsistent = new Achievement("","Inconsistent","Title with a 1 and 10 rating",3);
inconsistent.setShowable(false);
masterpiece = new Achievement("","True Masterpiece","Title with ten 10 ratings",1);
masterpiece.setShowable(false);

String hint = "If at first you don't succeed";
doubledip = new Achievement("","Double Dip","10 more reviews than media",3);
doubledip.setHint(hint);
dejavu = new Achievement("","Deja Vu","100 more reviews than media",2);
dejavu.setHint(hint);
timeloop = new Achievement("","Time Loop","1k more reviews than media",1);

String hint = "Double dipping isn't always bad";
iconic = new Achievement("","Iconic","Review 1 media 20 times",1);
iconic.setHint(hint);
classic = new Achievement("","Classic","Review 1 media 10 times",2);
classic.setHint(hint);
favorite = new Achievement("","Favorite","Review 1 media 5 times",3);

neverAgain = new Achievement("","Never Again","Give a 1 star review",3);
neverAgain.setShowable(false);

String hint = "Keep the doctor away";
fanatic = new Achievement("","Fanatic","1 review/day for 3 months",1);
fanatic.setHint(hint);
obsessed = new Achievement("","Obsessed","1 review/day for 2 months",2);
obsessed.setHint(hint);
hooked = new Achievement("","Hooked","1 review/day for a month",3);
hooked.setHint(hint);

hint = "Can't stop";
unemployed = new Achievement("","Unemployed","10 review/day for 20 days",1);
unemployed.setHint(hint);
vacation = new Achievement("","Vacation","10 review/day for a week",2);
vacation.setHint(hint);
weekend = new Achievement("","Weekend","10 review/day for 3 days",3);

rock = new Achievement("","Rock","Rock (or Earth) in a title",3);
rock.setShowable(false);
rock.setHint(hint);

paper = new Achievement("","Paper","Paper (or Book) in a title",3);
paper.setShowable(false);
paper.setHint(hint);

scissors = new Achievement("","Scissors","Scissor (or Blade) in a title",3);
scissors.setShowable(false);
scissors.setHint(hint);

lizard = new Achievement("","Lizard","Lizard (or Godzilla) in a title",3);
lizard.setShowable(false);
lizard.setHint(hint);

spock = new Achievement("","Spock","Star Trek (or Alien) in a title",3);
spock.setShowable(false);

String hint = "Those who retrospect together, stay together";
bff = new Achievement("","BFFs","1k reviews from a user",1);
bff.setHint(hint);
chum = new Achievement("","Chums","100 reviews from a user",2);
chum.setHint(hint);
friend = new Achievement("","Friends","10 reviews from a user",3);
friend.setHint(hint);

hint = "Ask your friends";
anthropologist = new Achievement("","Anthropologist","10k reviews from users",1);
anthropologist.setHint(hint);
famous = new Achievement("","Famous","1k reviews from users",2);
famous.setHint(hint);
popular = new Achievement("","Popular","100 reviews from users",3);
popular.setHint(hint);