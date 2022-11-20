package com.company;
//Janoth Ketheeswaran
//06/12/2021
//Version 1
//Create a program that allows the user to look after multiple pets
import java.io.*; //importing all libraries needed for program
import java.util.Random;
import java.util.Scanner;
//creating the game
public class Main {
    public static void main(String[] args) {
    startOption(); //begin with either the option to start a new game or load a game save
    System.exit(0);
    }
    public static void startOption(){
        switch(Integer.parseInt(askQuestion("Would you like to: (1) start a new game? (2) load save?"))){
            case 1:
                createFirstPet(createPetArray(), 0);//start a new game
                break;
            case 2:
                game(loadSave(createPetArray()), 0);//load a game save
                break;
            default:
                System.out.println("Please enter a valid option");//in case user inputs invalid option, program loops to ask again
                System.out.println("---------------------------");
                startOption();
                break;
        }
    }
    public static pet[] createPetArray(){//makes an array of pets all set to null/empty
        pet[] petArray = new pet[5];
        for (int i = 0; i<petArray.length; i++){
            petArray[i] = new pet();
            setName(petArray[i], "N/A(emptyPet)");
        }
        return petArray;
    }
    public static void createFirstPet(pet[] petArray, int position){//creating first pet for a new game
        setName(petArray[position], askQuestion("What do you want your first pet to be called?"));//setting all attributes to base/user desired
        setType(petArray[position], askQuestion("What do you want your first pet's type to be?"));
        setHunger(petArray[position], 0);
        setBoredom(petArray[position], 0);
        outputWelcomeMessage(petArray, position);//confirm that new pet has been made
        game(petArray, position);//move onto game now that we have an array of pets and our first pet
    }
    public static void createPet(pet[] petArray, int position){//creating a new pet
        setName(petArray[position], askQuestion("What do you want your pet to be called?"));//setting all attributes to base/user desired
        setType(petArray[position], askQuestion("What do you want your pet's type to be?"));
        setHunger(petArray[position], 0);
        setBoredom(petArray[position], 0);
        outputWelcomeMessage(petArray, position);//confirm that new pet has been made
    }
    public static void outputWelcomeMessage(pet[] petArray, int position){//output a message to confirm a pet has been made
        System.out.println("Welcome " + getName(petArray[position]) + "! They are such a beautiful " + getType(petArray[position]));
    }
    public static void game(pet[] petArray, int position){//the main game
        for (int k = 0; k<petArray.length; k++){
            System.out.println(petArray[k].name);
            System.out.println(petArray[k].type);
            System.out.println(petArray[k].hunger);
            System.out.println(petArray[k].boredom);
            System.out.println(petArray[k].combinedNeeds);
            System.out.println(petArray[k].isDead);
        }
        boolean petDead = false;//condition for stopping the game if all pets dead
        int deadCounter;//count of how many pets have died
        int[] strikes = new int[5];//keeps track of number of strikes for each pet
        pet[] urgentPets = createPetArray();//array for pets for sorting them into most urgent-least urgent
        while(!petDead){
            urgentPets = bubbleSort(urgentPets, petArray, position);//keep updating most urgent-least urgent pets as their stats change
            position = menu(petArray, urgentPets, position);//keep updating number of pets that exist as the game goes on
            deadCounter = 0;//reset number of pets dead so it doesn't carry over and falsely stop the game
            for (int i = 0; i<(position+1); i++){
                timeForward(petArray[i]);//increase hunger and boredom of each pet
                if (getHunger(petArray[i])>=10&&!getIsDead(petArray[i])) {//warn the player if a pet gets a strike
                    System.out.println(getName(petArray[i]) + " is too hungry! They start eating the carpet and get full.");
                    setHunger(petArray[i], 0);
                    strikes[i]++;
                }
                if (getBoredom(petArray[i])>=10&&!getIsDead(petArray[i])) {//warn the player if a pet gets a strike
                    System.out.println(getName(petArray[i]) + " is too bored! They run laps around the house and get tired.");
                    setBoredom(petArray[i], 0);
                    strikes[i]++;
                }
                if (strikes[i] >= 2&&!getIsDead(petArray[i])) {//pet dies when it has 2 strikes
                    System.out.println(getName(petArray[i]) + " has died due to your ignorance.");
                    setIsDead(petArray[i], true);//declare pet as dead
                    for (int j = 0; j<(position+1); j++){//count number of pets dead
                        if(getIsDead(petArray[i])){
                            deadCounter++;
                        }
                        if(deadCounter==(position+1)){//if all of them are dead, stop the game
                            petDead = true;
                        }
                    }
                }
            }
        }
        System.out.println("Game over. All pets have died.");//output a game over message at the end of the game
    }
    public static int menu(pet[] petArray, pet[] urgentPets, int numOfPets) {//where each round of game occurs
        int petChosen;
        displayOptions();//ask the player what they want to do
        switch(Integer.parseInt(askQuestion("What do you want to do?"))){
            case 1:
                petChosen = askWhichPet(petArray, numOfPets);//ask which pet they want info on
                if(!getIsDead(petArray[petChosen])&&!getName(petArray[petChosen]).equals("N/A(emptyPet)")){//only output info of pet if it exists and is not dead
                    System.out.println("-----------------------");
                    System.out.println(getName(petArray[petChosen]) + " is a " + getType(petArray[petChosen]));
                    System.out.println("Their hunger is: " + getHunger(petArray[petChosen]));
                    System.out.println("Their boredom is: " + getBoredom(petArray[petChosen]));
                    System.out.println("-----------------------");
                }
                else{//if the pet is either dead or doesn't exist, let the player know of this
                    System.out.println("This pet is either dead or does not exist!");
                }
                break;
            case 2:
                System.out.println("-----------------------");
                for (int i = 0; i<urgentPets.length; i++){
                    if(!getName(urgentPets[i]).equals("N/A(emptyPet)")){
                        if(!getIsDead(urgentPets[i])){//make sure the pets are alive and exist before outputting their needs
                            System.out.println(getName(urgentPets[i]) + "'s needs amount to: " + getCombinedNeeds(urgentPets[i]));//output each pet's needs
                        }
                    }
                }
                System.out.println("-----------------------");
                if(getHunger(urgentPets[0])>getBoredom(urgentPets[0])){//output the most urgent pet in particular to warn the player of what they need
                    System.out.println("You should pay particular attention to " + getName(urgentPets[0]) + "! They are hungry!");
                }
                else if(getHunger(urgentPets[0])<getBoredom(urgentPets[0])){
                    System.out.println("You should pay particular attention to " + getName(urgentPets[0]) + "! They are bored!");
                }
                else if(getHunger(urgentPets[0])==getBoredom(urgentPets[0])){
                    System.out.println("You should pay particular attention to " + getName(urgentPets[0]) + "! They are hungry and bored!");
                }
                System.out.println("-----------------------");
                break;
            case 3:
                petChosen = askWhichPet(petArray, numOfPets);//ask which pet they want to feed
                if(!getIsDead(petArray[petChosen])&&!getName(petArray[petChosen]).equals("N/A(emptyPet)")){
                    feedPetFood(petArray[petChosen]);//if the pet is alive and exists, feed it
                }
                else{//if the pet is either dead or does not exist, let the player know of this
                    System.out.println("This pet is either dead or does not exist!");
                }
                break;
            case 4:
                petChosen = askWhichPet(petArray, numOfPets);//ask which pet they want to play with
                if(!getIsDead(petArray[petChosen])&&!getName(petArray[petChosen]).equals("N/A(emptyPet)")){
                    playPet(petArray[petChosen]);//if the pet is alive and exists, play with it
                }
                else{//if the pet is either dead or does not exist, let the player know of this
                    System.out.println("This pet is either dead or does not exist!");
                }
                break;
            case 5:
                if(!checkMax(petArray)) {
                    numOfPets++;//if the max number of pets has not been reached, make a new pet and increment the number of pets in existence
                    createPet(petArray, numOfPets);
                }
                else{//if the max number of pets has been reached, let the player know of this
                    System.out.println("You can't make anymore pets, you have reached the maximum!");
                }
                break;
            case 6:
                saveFileAndExit(petArray);//save current data and end the program
                break;
            case 7:
                loadSave(petArray);//load save data
                break;
            default://if user inputs invalid option, program loops to ask again
                System.out.println("Please enter a valid option");
                menu(petArray, urgentPets, numOfPets);
                break;
        }
        int numOfPetsCheck = 0;//at the end of each round, update the number of alive pets in existence
        for (int i = 0; i<petArray.length; i++){
            if(!getName(petArray[i]).equals("N/A(emptyPet)")){
                numOfPetsCheck++;
            }
        }
        numOfPets=numOfPetsCheck-1;
        return numOfPets;//update number of pets
    }
    public static pet[] loadSave(pet[] petArray){//load save data
        try{
            BufferedReader inputStream = new BufferedReader(new FileReader("petProjectSaveData.txt"));
            String wholeFile = inputStream.readLine();//file is read from and stored in this string
            int lengthSoFar = 0;
            int whichNodeToAssign = 0;
            String[] infoFromFile = new String[30];//array to store every value from file in organised format
            for (int i = 0;i<wholeFile.length();i++){//split string at split points and store each value in infoFromFile
                if(wholeFile.charAt(i)=='.'){
                    lengthSoFar = i;
                }
                if(wholeFile.charAt(i)==','){
                    infoFromFile[whichNodeToAssign]=wholeFile.substring((lengthSoFar+1),(i));
                    lengthSoFar = i;
                    whichNodeToAssign++;
                }
            }
            int whichPet = 0;
            int j = 0;
            while(j!=infoFromFile.length){//store values from infoFromFile into petArray
                setName(petArray[whichPet], infoFromFile[j]);
                setType(petArray[whichPet], infoFromFile[j+1]);
                setHunger(petArray[whichPet], Integer.parseInt(infoFromFile[j+2]));
                setBoredom(petArray[whichPet], Integer.parseInt(infoFromFile[j+3]));
                setCombinedNeeds(petArray[whichPet], Integer.parseInt(infoFromFile[j+4]));
                setIsDead(petArray[whichPet], Boolean.parseBoolean(infoFromFile[j+5]));
                whichPet++;
                j=j+6;
            }
            System.out.println("Game save successfulyy loaded!");//output message to confirm successful load
        }catch(IOException error){
            System.out.println(error);
        }
        return petArray;//update the petArray with the loaded values
    }
    public static void saveFileAndExit(pet[] petArray){//save data and end the program
        try{
            PrintWriter outputStream = new PrintWriter(new FileWriter("petProjectSaveData.txt"));
            for (int i=0;i<petArray.length;i++){//save all data from petArray in a single line with split points
                outputStream.print("."+getName(petArray[i])+","+getType(petArray[i])+","+getHunger(petArray[i])+","+getBoredom(petArray[i])+","+getCombinedNeeds(petArray[i])+","+getIsDead(petArray[i])+",");
            }
            outputStream.print("!");//end the file with a unique split point
            outputStream.close();//close the file
            System.out.println("Game successfully saved!");//confirm that game save was successful
            System.exit(0);
        }catch(IOException error){
            System.out.println(error);
        }
    }
    public static pet[] bubbleSort(pet[] arrayToSort, pet[] arrayFromGame, int numOfPets){//sort pets into most-least urgent
        for (int i = 0; i<arrayToSort.length; i++){//calculate combined needs of each pet
            arrayToSort[i]=arrayFromGame[i];
            setCombinedNeeds(arrayToSort[i], ((getHunger(arrayToSort[i]))+(getBoredom(arrayToSort[i]))));
        }
        boolean swapHasOcurred = true;
        int swapCounter;
        pet tempSwapPet;
        while(swapHasOcurred) {
            swapCounter=0;//bubble sort the pets in terms of most combined needs to least combined needs
            for (int j = 0; j < (numOfPets+1); j++) {
                if(getCombinedNeeds(arrayToSort[j])<getCombinedNeeds(arrayToSort[j+1])){
                    tempSwapPet = arrayToSort[j];
                    arrayToSort[j] = arrayToSort[j+1];
                    arrayToSort[j+1] = tempSwapPet;
                    swapCounter++;
                }
            }
            if(swapCounter==0){
                swapHasOcurred=false;
            }
        }
        return arrayToSort;//return the sorted array
    }
    public static boolean checkMax(pet[] petArray){//check if the max number of pets has been reached
        boolean isFull = true;
        for (int i = 0; i<petArray.length; i++){
            if(getName(petArray[i]).equals("N/A(emptyPet)")){//if a non-existent pet exists in the array, it is not full
                isFull=false;
            }
        }
        return isFull;//return whether the array is full or not
    }
    public static int askWhichPet(pet[] petArray, int numOfPets){//confirm which pet the user wants to tend to
        System.out.println("============================");
        int petNum = Integer.parseInt(askQuestion("Which pet would you like to tend to?"));
        if(!getIsDead(petArray[petNum-1])&&!getName(petArray[petNum-1]).equals("N/A(emptyPet)")){
            return (petNum-1);//allows the user to tend to an existent alive pet
        }
        System.out.println("This pet is either dead or does not exist!");
        return askWhichPet(petArray, numOfPets);//let user know if the pet is dead or non-existent
    }
    public static void timeForward(pet Pet){//progress the rounds
        setHunger(Pet, (getHunger(Pet)+1));//increases pet hunger and boredom by 1 every round (action the player does)
        setBoredom(Pet, (getBoredom(Pet)+1));
    }
    public static void displayOptions(){//options available to the player every round
        System.out.println("---------------");
        System.out.println("1=get info (name, hunger, boredom) on pet");
        System.out.println("2=check needs of all pets");
        System.out.println("3=feed pet food");
        System.out.println("4=play with pet");
        System.out.println("5=create new pet");
        System.out.println("6=save and exit");
        System.out.println("7=load save");
        System.out.println("---------------");
    }
    public static String getName(pet Pet){return Pet.name;}//accessor methods for attributes of pet class
    public static String getType(pet Pet){return Pet.type;}
    public static int getHunger(pet Pet){return Pet.hunger;}
    public static int getBoredom(pet Pet){return Pet.boredom;}
    public static int getCombinedNeeds(pet Pet){return Pet.combinedNeeds;}
    public static boolean getIsDead(pet Pet){return Pet.isDead;}
    public static void setName(pet Pet, String name){
        Pet.name = name;
    }
    public static void setType(pet Pet, String type){
        Pet.type = type;
    }
    public static void setHunger(pet Pet, int hunger){
        Pet.hunger = hunger;
    }
    public static void setBoredom(pet Pet, int boredom){
        Pet.boredom = boredom;
    }
    public static void setCombinedNeeds(pet Pet, int combinedNeeds){Pet.combinedNeeds = combinedNeeds;}
    public static void setIsDead(pet Pet, boolean isDead){
        Pet.isDead = isDead;
    }
    public static void feedPetFood(pet Pet){//allow the user to feed a pet
        if(getHunger(Pet)==0){//let the user know if their pet is full
            System.out.println("Your pet is already full!");
        }
        else if(getHunger(Pet)==1){//if the pet only has 1 hunger, deduct only 1 hunger
            setHunger(Pet, (getHunger(Pet)-1));
            System.out.println("You feed your pet some food (-1 hunger)");
        }
        else{//otherwise, feeding deducts either 1 or 2 hunger
            int negate = randomInt(1, 3);
            setHunger(Pet, (getHunger(Pet)-negate));
            System.out.println("You feed your pet some food (-" + negate + " hunger)");
        }
    }
    public static void playPet(pet Pet){//allow the user to play with a pet
        if(getBoredom(Pet)==0){//let the user know if their pet is not bored
            System.out.println("Your pet is not bored at the moment!");
        }
        else if(getBoredom(Pet)==1){//if the pet only has 1 boredom, deduct only 1 boredom
            setBoredom(Pet, (getBoredom(Pet)-1));
            System.out.println("You play tag with your pet (-1 boredom)");
        }
        else{//otherwise, playing deducts either 1 or 2 boredom
            int negate = randomInt(1, 3);
            setBoredom(Pet, (getBoredom(Pet)-negate));
            System.out.println("You play tag with your pet (-" + negate + " boredom)");
        }
    }
    public static int randomInt(int boundLow, int boundHigh){//generate a random integer between 2 limits
        Random r = new Random();
        return r.nextInt(boundLow, boundHigh);
    }
    public static String askQuestion(String question){//allow user input
        Scanner myScanner = new Scanner(System.in);
        System.out.println(question);//ask the user a question
        return myScanner.nextLine();//return their answer
    }
}
