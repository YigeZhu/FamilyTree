import java.util.*;
/**
 * A collection of family member,represents the whole family tree
 *
 * @Destiny Zhu, Melory So, Coco Kneer
 * @05/13/2018
 */
public class Family//implements Graph<T>
{
    private AdjListsWeightedGraph<FamilyMember> familyTree;
    private LinkedList<FamilyMember> members; //a linked list to keep track of order of the family members in terms of age;
    private FamilyMember user;

    /**
     * Constructor that creates an empty family object
     */
    public Family(){
        familyTree = new AdjListsWeightedGraph<FamilyMember>();
        members = new LinkedList<FamilyMember>();
    }

    /**
     * Family class constructor
     * Takes a FamilyMember as input (represented as the first created person
     * in the program) and sets them as the user
     */
    public Family(FamilyMember user){
        familyTree = new AdjListsWeightedGraph<FamilyMember>();
        members = new LinkedList<FamilyMember>();
        familyTree.addVertex(user);
        members.add(user);
        this.user = user;
    }

    /**
     * set the user as family imput
     * 
     * param FamilyMember user
     * return void
     */
    public void setUser(FamilyMember user) {
        this.user = user;
        familyTree.addVertex(user);
        members.add(user);
    }

    /**
     * get the user 
     * 
     * param no
     * return FamilyMember user
     */
    public FamilyMember getUser() {
        return user;
    }    

    /**
     * Adding a parent to the graph.
     * Assumes (a) people have the same parents as their siblings and 
     * (b) people with the same child are spouses, setting a relationship between them as well
     * 
     * param FamilyMember child, FamilyMember parent
     * return void
     */
    public void addParent(FamilyMember child, FamilyMember parent){
        familyTree.addVertex(parent);
        familyTree.addArc(parent, child, 1);

        //linking siblings to parents and parents as spouses
        for(FamilyMember member: familyTree.getVertices()) {
            //finds siblings that are not connected yet
            if(isSibling(child, member) && !familyTree.isArc(parent, member)) {
                familyTree.addArc(parent, member, 1);
            }

            //sets parents as spouses
            if((isMother(child, member) || isFather(child, member)) && member != parent) {
                member.setSpouse(parent);
                parent.setSpouse(member);

                familyTree.addEdge(parent, member, 0);
            }
        }

        if (!members.contains(parent)) members.add(parent);
    }

    /**
     * Adding a spouse to the graph.
     * Assumes spouses share the same children
     * 
     * param FamilyMember current, FamilyMember spouse
     * return void
     */
    public void addSpouse(FamilyMember current, FamilyMember spouse){
        familyTree.addVertex(spouse);
        familyTree.addEdge(current, spouse, 0);

        current.setSpouse(spouse);  //set each other as spouses
        spouse.setSpouse(current);

        //System.out.println("children: " + getChildren(current));
        //add the children of the current to the spouse
        for(FamilyMember child: getChildren(current)) {
            familyTree.addArc(spouse, child, 1);
        }

        if (!members.contains(spouse)) members.add(spouse);
    }

    /**
     * Adding a sibling to the graph.
     * Assumes siblings have same parents and siblings and sets a relationship between
     * them as well
     * 
     * param FamilyMember current, FamilyMember sibling
     * return void
     */
    public void addSibling(FamilyMember current, FamilyMember sibling){
        familyTree.addVertex(sibling);
        familyTree.addEdge(sibling, current, 0);

        for(FamilyMember member: familyTree.getVertices()) {
            //finds parents that are not connected yet
            //add arcs between sibling and both parents
            if((isMother(current, member) || isFather(current, member))
            && !familyTree.isArc(member, sibling)) {
                addParent(current, member);
            }
            //add arcs between sibling and current's existing siblings
            if(isSibling(current, member) && sibling != member && sibling != current){
                familyTree.addEdge(member, sibling, 0);
            }
        }

        if (!members.contains(sibling)) members.add(sibling);
    }

    /**
     * Adding a child to graph.
     * Assumes spouse is parent to children as well and all children are 
     * siblings
     * 
     * param FamilyMember parent, FamilyMember child
     * return void
     */
    public void addChild(FamilyMember parent, FamilyMember child){
        familyTree.addVertex(child);
        familyTree.addArc(parent, child, 1);

        FamilyMember spouse = parent.getSpouse();
        familyTree.addArc(spouse, child, 1); //add an arc between child and parent's spouse

        for(FamilyMember member: familyTree.getVertices()) {
            //finds children that are not connected yet
            //add arcs between child and his/her siblings
            if((isMother(member, parent) || isFather(member, parent))
            && !familyTree.isArc(member, child) && member!=child) {
                addSibling(member, child);
            }
        }

        if (!members.contains(child)) members.add(child);
    }

    /**
     * Returns true if second family member input is the mother of 
     * first family member input
     * 
     * param FamilyMember current, FamilyMember mom
     * return true if mom is the mother of current
     */
    public boolean isMother(FamilyMember current, FamilyMember mom){
        //a person is mother if the generation gap is 1 and she is a female and
        //there is an direct arc between her and current
        return (isFemale(mom) && getGeneration(mom, current) == 1 && 
            familyTree.isArc(mom, current));
    }

    /**
     * Returns true if second family member input is the father of 
     * first family member input
     * 
     * param FamilyMember current, FamilyMember dad
     * return true if dad is the father of current
     */
    public boolean isFather(FamilyMember current, FamilyMember dad){
        //a person is father if the generation gap is 1 and he is not a female and
        //there is an direct arc between him and current
        return (!isFemale(dad) && getGeneration(dad, current) == 1 &&
            familyTree.isArc(dad, current));
    }

    /**
     * Returns if second family member input is the spouse of 
     * first family member input, returns false if person has no spouse
     * 
     * param FamilyMember current, FamilyMember spouse
     * return true if spouse is the spouse of the current, false if person has no spouse
     */
    public boolean isSpouse(FamilyMember current, FamilyMember spouse){
        //returns false if person has no spouse
        if(current.getSpouse() != null) {
            return current.getSpouse() == spouse;
        }

        return false;
    }

    /**
     * Returns true if second family member input is the sibling of 
     * first family member input
     * 
     * param FamilyMember current, FamilyMember sibling
     * return true if sibling is the sibling of the current
     */
    public boolean isSibling(FamilyMember current, FamilyMember sibling){
        //a person is sibling when the generation gap is 0, he/she not the spouse of the
        //current, there is an direct arc between he/she and current and he/she is not current
        return (!isSpouse(current, sibling) && getGeneration(current, sibling) == 0 
            && !current.equals(sibling) && familyTree.isArc(current,sibling));
    }

    /**
     * get the children of a family member
     * 
     * param FamilyMember person
     * return LinkedList<FamilyMember> children
     */
    public LinkedList<FamilyMember> getChildren(FamilyMember person){
        LinkedList<FamilyMember> children = new LinkedList<FamilyMember>();
        LinkedList<FamilyMember> successors = familyTree.getSuccessors(person);
        for(int i=0; i < successors.size(); i++){
            if(familyTree.getWeight(person, successors.get(i)) == 1) //weight of the arc between children and the person is 1
                children.add(successors.get(i));
        }
        return children;
    }

    /**
     * return true if the family member is from the mother side
     * 
     * param FamilyMember member
     * return true if the family member is from the mother side
     */
    public boolean isMaternal(FamilyMember member){
        //access to mother
        FamilyMember mother = new FamilyMember();
        LinkedList<FamilyMember> maternals = new LinkedList<FamilyMember>(); //people linked to mother
        for(FamilyMember person: familyTree.getVertices()){
            if(isMother(user, person)) {
                mother = person;
                maternals = familyTree.getPredecessors(mother);
            }
        }

        maternals.remove(mother.getSpouse());

        LinkedList<FamilyMember> temp = new LinkedList<FamilyMember>();
        //gets cousins and respective spouses from aunts/uncles' successor
        for (FamilyMember person: maternals){
            if(isSibling(mother, person)) {
                //System.out.println(person);
                //System.out.println(getChildren(person));
                temp.addAll(getChildren(person));

                if(person.getSpouse() != null) {
                    temp.add(person.getSpouse());
                }
            }
        }

        maternals.addAll(temp);
        //System.out.println(maternals); 
        return (maternals.indexOf(member) >= 0);
    }

    /**
     * get the generation gap between two family member
     * 
     * param FamilyMember older, FamilyMember younger
     * return int the generation gap
     */
    public int getGeneration(FamilyMember older, FamilyMember younger){
        LinkedList<FamilyMember> path = familyTree.getPath(older, younger);

        int result = 0;

        //adding the weights together
        for(int i=0; i<path.size() - 1; i++){
            result += familyTree.getWeight(path.get(i), path.get(i+1));
        }
        return result;
    }

    /**
     * Getter. get instance variable familyTree
     * 
     * param no
     * return AdjListsWeightedGraph<FamilyMember> familyTree
     */
    public AdjListsWeightedGraph<FamilyMember> getFamilyTree(){
        return this.familyTree;
    }

    /**
     * return true if the family member is female
     * 
     * param FamilyMember person
     * return true if the family member is female
     */
    public boolean isFemale(FamilyMember person){
        return person.getGender().equals("female");
    }

    /**
     * get the title of a family member with respect to user
     * 
     * param FamilyMember person
     * return String the title
     */
    public String getTitle(FamilyMember person){
        int genGap = getGeneration(person, user);
        String result = "";
        if(person.equals(user))
            result = "Wo";
        else if (isMother(user, person))
            result = "Ma Ma";
        else if (isFather(user, person))
            result =  "Ba Ba";
        else if (genGap == 1){ //parents' siblings
            if(isMaternal(person) && isFemale(person)) // mom's sister
                result =  "Yi Ma";
            else if (isMaternal(person) && !isFemale(person)) // mom's brother
                result =  "Jiu Jiu";
            else if (!isMaternal(person) && isFemale(person)) //dad's sister
                result =  "Gu Gu";
            else //dad's brother
                result =  "Bo Bo";
        }
        else if (genGap == 2){ //grandparents
            if(isMaternal(person) && isFemale(person)) //mom's mother
                result =  "Wai Po";
            else if (isMaternal(person) && !isFemale(person)) //mom's father
                result =  "Wai Gong";
            else if (!isMaternal(person) && isFemale(person)) //dad's mother
                result =  "Nai Nai";
            else //dad's father
                result =  "Ye Ye";
        }
        else { //siblings
            if(isFemale(person) && user.isOlder(person)) //user's little sister
                result =  "Mei Mei";
            else if(isFemale(person) && !user.isOlder(person)) //user's big sister
                result =  "Jie Jie";
            else if(!isFemale(person) && user.isOlder(person)) //user's little brother
                result =  "Di Di";
            else //user's big brother
                result =  "Ge Ge";
        }

        //spouse of parents' siblings
        try{
            String spouseName = person.getSpouse().getTitle();
            if(spouseName.equals("Jiu Jiu")) //wife of mom's brother
                result = "Jiu Ma";
            else if(spouseName.equals("Yi Ma")) //husband of mom's sister
                result = "Yi Fu";
            else if(spouseName.equals("Gu Gu")) //husband of dad's sister
                result = "Gu Fu";
            else if(spouseName.equals("Bo Bo")) //wife of dad's brother
                result = "Bo Mu";
        }catch(NullPointerException e){
        }

        person.setTitle(result);

        return result;
    }

    /**
     * return a string representation of the Family class
     * 
     * param no
     * return String the string representation
     */
    public String toString(){
        return familyTree.toString();
    }
    
    /**
     * Returns an array of names of all family members.
     * 
     * param no
     * return array of names
     */
    public String[] getMembersArray(){
        //FamilyMember[] temp = members.toArray(new FamilyMember[members.size()]);
        String[] result = new String[members.size()+1];
        result[0] = "...";
        for (int i=0;i<members.size();i++){
            result[i+1] = members.get(i).getName();
        }
        return result;
    }
    
    /**
     * Returns a LinkedList of all family members.
     * @return LinkedList of all family members
     */
    public LinkedList<FamilyMember> getMembersLinkedList(){
        //FamilyMember[] temp = members.toArray(new FamilyMember[members.size()]);
        return members;
    }
    
    /**
     * Takes a String of name and returns the corresponding FamilyMember object.
     * @param String name
     * @return FamilyMember
     */
    public FamilyMember getMember(String name){
        int index = 0;
        for (int i=0;i<members.size();i++){
            if (members.get(i).getName().equals(name)){
                index = i;break;
            }
        }
        return members.get(index);
    }

    public static void main(String[] args){
        FamilyMember me = new FamilyMember("Me", "Wo",11,9,1999,"female");
        FamilyMember sisterBig = new FamilyMember("Sister", "Jie", 5, 11, 1997, "female");
        FamilyMember sisterLittle = new FamilyMember("Sister", "Mei", 8, 4, 2003, "female");
        FamilyMember brotherLittle = new FamilyMember("Brother", "Di", 5, 11, 2001, "male");
        FamilyMember brotherBig = new FamilyMember("Brother", "Ge", 3, 16, 1995, "male");
        FamilyMember mother = new FamilyMember("Mom", "Ma",9,11,1968,"female");
        FamilyMember father = new FamilyMember("Dad", "Ba",29,11,1967,"male");

        FamilyMember uncleP = new FamilyMember("UncleP", "Bo", 1, 1, 1962, "male");
        FamilyMember uncleM = new FamilyMember("UncleM", "Jiu", 1, 5, 1962, "male");
        FamilyMember auntP = new FamilyMember("AuntP", "Gu",5,11,1970,"female");
        FamilyMember auntM = new FamilyMember("AuntM", "Yi",5,11,1970,"female");

        FamilyMember grandfatherP = new FamilyMember("GrandpaP", "Ye",31,7,1934,"male");
        FamilyMember grandmotherP = new FamilyMember("GrandmaP", "Nai",31,8,1935,"female");
        FamilyMember grandfatherM = new FamilyMember("GrandpaM", "Gong",31,8,1935,"male");
        FamilyMember grandmotherM = new FamilyMember("GrandmaM", "Po",31,8,1935,"female");

        FamilyMember cousin = new FamilyMember("Cousin", "Biao", 9, 11, 2001, "female");
        FamilyMember cousin2 = new FamilyMember("Cousin2", "Biao2", 9, 11, 2005, "male");
        FamilyMember cousin3 = new FamilyMember("Cousin3", "Biao3", 1, 19, 1995, "male");

        Family family1 = new Family(me);

        System.out.println(family1);

        System.out.println("\n--Testing addParent method--");
        family1.addParent(me, mother);
        family1.addParent(me, father);
        family1.addParent(father, grandfatherP);
        family1.addParent(father, grandmotherP);
        family1.addParent(mother, grandmotherM);

        System.out.println(family1);

        System.out.println("\n--Testing addSibling method--");
        family1.addSibling(me, sisterBig);
        family1.addSibling(me, sisterLittle);
        family1.addSibling(sisterLittle, brotherBig);
        family1.addSibling(sisterBig, brotherLittle);
        family1.addSibling(father, uncleP);

        System.out.println(family1);

        System.out.println("\n--Testing addChild, addSibling and addSpouse method--");
        family1.addChild(grandmotherM, auntM);
        family1.addChild(auntM, cousin3);
        family1.addSibling(auntM, uncleM);
        family1.addChild(uncleP, cousin);
        family1.addSibling(cousin, cousin2);
        family1.addSpouse(uncleP, auntP);
        family1.addSpouse(grandmotherM, grandfatherM);

        System.out.println(family1);

        System.out.println("\n--Testing getGeneration--");
        System.out.println("From me to me. Expected: 0, Actual " + family1.getGeneration(me, me));
        System.out.println("From me to brotherLittle. Expected: 0, Actual " + family1.getGeneration(me, brotherLittle));
        System.out.println("From mother to me. Expected: 1, Actual " + family1.getGeneration(mother, me));
        System.out.println("From auntM to me. Expected: 1, Actual " + family1.getGeneration(auntM, me));
        System.out.println("From grandfatherP to me. Expected: 2, Actual " + family1.getGeneration(grandfatherP, me));
        System.out.println("From grandfatherP to cousin3. Expected: 2, Actual " + family1.getGeneration(grandfatherP, cousin3));
        System.out.println("From grandfatherM to father. Expected: 1, Actual " + family1.getGeneration(grandfatherM, father));

        System.out.println("\n--Testing isMother method--"); 
        System.out.println("me, mother. Expected: true, Actual: " + family1.isMother(me, mother));
        System.out.println("brotherLittle, mother. Expected: true, Actual: " + family1.isMother(brotherLittle, mother));
        System.out.println("mother, me. Expected: false, Actual: " + family1.isMother(mother, me));
        System.out.println("uncleM, grandmotherM. Expected: true, Actual: " + family1.isMother(uncleM, grandmotherM));
        System.out.println("uncleM, grandfatherM. Expected: false, Actual: " + family1.isMother(uncleM, grandfatherM));
        System.out.println("mother, grandmotherP. Expected: false, Actual: " + family1.isMother(mother, grandmotherP));
        System.out.println("auntP, grandmotherP. Expected: false, Actual: " + family1.isMother(auntP, grandmotherP));

        System.out.println("\n--Testing isFather method--");
        System.out.println("me, father. Expected: true, Actual: " + family1.isFather(me, father));
        System.out.println("brotherLittle, mother. Expected: false, Actual: " + family1.isFather(brotherLittle, mother));
        System.out.println("father, me. Expected: false, Actual: " + family1.isFather(father, me));
        System.out.println("uncleM, grandfatherM. Expected: true, Actual: " + family1.isFather(uncleM, grandfatherM));
        System.out.println("uncleM, grandmotherM. Expected: false, Actual: " + family1.isFather(uncleM, grandmotherM));
        System.out.println("mother, grandfatherP. Expected: false, Actual: " + family1.isFather(mother, grandfatherP));
        System.out.println("auntP, grandmotherP. Expected: false, Actual: " + family1.isFather(auntP, grandmotherP));

        System.out.println("\n--Testing isSpouse method--");
        System.out.println("father, mother. Expected: true, Actual: " + family1.isSpouse(father, mother));
        System.out.println("sisterBig, me. Expected: false, Actual: " + family1.isSpouse(sisterBig, me));
        System.out.println("auntP, uncleP Expected: true, Actual: " + family1.isSpouse(auntP, uncleP));
        System.out.println("auntM, uncleM. Expected: false, Actual: " + family1.isSpouse(auntM, uncleM));
        System.out.println("grandfatherM, grandmotherM. Expected: true, Actual: " + family1.isSpouse(grandfatherM, grandmotherM));
        System.out.println("grandfatherP, grandmotherM. Expected: false, Actual: " + family1.isSpouse(grandfatherP, grandmotherM));

        System.out.println("\n--Testing isSibling method--");
        System.out.println("brotherLittle, sisterBig. Expected: true, Actual: " + family1.isSibling(brotherLittle, sisterBig));
        System.out.println("mother, father. Expected: false, Actual: " + family1.isSibling(mother, father));
        System.out.println("father, uncleP. Expected: true, Actual: " + family1.isSibling(father, uncleP));
        System.out.println("cousin, cousin2. Expected: true, Actual: " + family1.isSibling(cousin, cousin2));
        System.out.println("cousin, cousin3. Expected: false, Actual: " + family1.isSibling(cousin, cousin3));
        System.out.println("auntP, uncleP. Expected: false, Actual: " + family1.isSibling(auntP, uncleP));
        System.out.println("auntM, uncleM. Expected: true, Actual: " + family1.isSibling(auntM, uncleM));
        System.out.println("grandfatherM, grandmotherM. Expected: false, Actual: " + family1.isSibling(grandfatherM, grandmotherM));
        System.out.println("grandfatherP, grandmotherM. Expected: false, Actual: " + family1.isSibling(grandfatherP, grandmotherM));

        System.out.println("\n--Testing getChildren--");
        System.out.println("Mother: Expected: Wo, Jie, Mei, Ge, Di. Actual: " + family1.getChildren(mother));
        System.out.println("GrandmotherM: Expected: Ma, Yi, Jiu. Actual: " + family1.getChildren(grandmotherM));
        System.out.println("GrandfatherP: Expected: Ba, Bo. Actual: " + family1.getChildren(grandfatherP));
        System.out.println("AuntM: Expected: Biao3" + family1.getChildren(auntM));
        System.out.println("AuntP: Expected: Biao, Biao2. Actual: " + family1.getChildren(auntP));

        System.out.println("\n--Testing isMaternal method");
        System.out.println("auntM Expected: true, Actual: " + family1.isMaternal(auntM));
        System.out.println("cousin3 Expected: true, Actual: " + family1.isMaternal(cousin3));
        System.out.println("auntP Expected: false, Actual: " + family1.isMaternal(auntP));
        System.out.println("cousin Expected: false, Actual: " + family1.isMaternal(cousin));
        System.out.println("grandmotherM Expected: true, Actual: " + family1.isMaternal(grandmotherM));
        System.out.println("grandfatherP Expected: false, Actual: " + family1.isMaternal(grandfatherP));
        System.out.println("father Expected: false, Actual: " + family1.isMaternal(father));

        System.out.println("\n--Testing isFemale method");
        System.out.println("Expected: true, Actual: " + family1.isFemale(auntM));
        System.out.println("Expected: false, Actual: " + family1.isFemale(cousin3));
        System.out.println("Expected: true, Actual: " + family1.isFemale(auntP));
        System.out.println("Expected: true, Actual: " + family1.isFemale(cousin));
        System.out.println("Expected: true, Actual: " + family1.isFemale(grandmotherM));
        System.out.println("Expected: false, Actual: " + family1.isFemale(grandfatherP));
        System.out.println("Expected: false, Actual: " + family1.isFemale(father));

        System.out.println("\n--Testing getTitle method");
        System.out.println("Mother: Expected: Ma Ma, Actual: " + family1.getTitle(mother));
        System.out.println("Father: Expected: Ba Ba, Actual: " + family1.getTitle(father));
        System.out.println("Big sister: Expected: Jie Jie, Actual: " + family1.getTitle(sisterBig));
        System.out.println("Little sister: Expected: Mei Mei, Actual: " + family1.getTitle(sisterLittle));
        System.out.println("Big brother: Expected: Ge Ge, Actual: " + family1.getTitle(brotherBig));
        System.out.println("Little brother: Expected: Di Di, Actual: " + family1.getTitle(brotherLittle));
        System.out.println("UncleP: Expected: Bo Bo, Actual: " + family1.getTitle(uncleP));
        System.out.println("UncleM: Expected: Jiu Jiu, Actual: " + family1.getTitle(uncleM));
        System.out.println("AuntP: Expected: Bo Mu, Actual: " + family1.getTitle(auntP));
        System.out.println("AuntM: Expected: Yi Ma, Actual: " + family1.getTitle(auntM));
        System.out.println("GrandpaP: Expected: Ye Ye, Actual: " + family1.getTitle(grandfatherP));
        System.out.println("GrandmaP: Expected: Nai Nai, Actual: " + family1.getTitle(grandmotherP));
        System.out.println("GrandpaM: Expected: Wai Gong, Actual: " + family1.getTitle(grandfatherM));
        System.out.println("GrandmaM: Expected: Wai Po, Actual: " + family1.getTitle(grandmotherM));
    }
}

