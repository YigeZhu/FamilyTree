/**
 * Represents each individual family member
 *
 * @Destiny Zhu, Melory So, Coco Kneer
 * @05/13/2018
 */
public class FamilyMember
{
    private String firstName, lastName, gender, title; 
    private int bDay, bMonth, bYear;
    String profileImage;
    private FamilyMember spouse;
    /**
     * Constructor for the FamilyMember Class
     */
    public FamilyMember(){
    }

    /**
     * Constructor for the FamilyMember Class
     */
    public FamilyMember(String fName, String lName, int bDay, int bMonth, int bYear, String gender)
    {
        this.firstName = fName;
        this.lastName = lName;
        this.bDay = bDay;
        this.bMonth = bMonth;
        this.bYear = bYear;
        this.gender = gender;
        // this.spouse = new FamilyMember();
        // this.title = "";
    }

    /**
     * Return the whole name of the person
     *
     * @param  no param
     * @return the name
     */
    public String getName(){
        return (firstName + " " + lastName);
    }

    /**
     * Set the spouse of a family member
     *
     * @param  no param
     * @return no return
     */
    public void setSpouse(FamilyMember person){
        spouse = person;
        person.spouse = this;
    }

    /**
     * get the spouse of a family member
     *
     * @param  no
     * @return FamilyMember person
     */
    public FamilyMember getSpouse(){
        return spouse;
    }

    /**
     * get the gender of a family member
     *
     * @param  no
     * @return String "female" or "male"
     */
    public String getGender(){
        return gender;
    }

    /**
     * set the gender of a family member
     *
     * @param  String gender
     * @return no
     */
    public void setGender(String gender){
        this.gender = gender;
    }

    /**
     * set the title of a family member
     *
     * @param  String title
     * @return no
     */
    public void setTitle(String title){
        this.title = title;
    }

    /**
     * get the title of a family member
     *
     * @param  no
     * @return String title
     */
    public String getTitle(){
        return title;
    }

    /**
     * get the birth year of a family member
     *
     * @param  no
     * @return int birth year
     */
    public int getBYear(){
        return bYear;
    }

    /**
     * get the birth month of a family member
     *
     * @param  no
     * @return int birth month
     */
    public int getBMonth(){
        return bMonth;
    }

    /**
     * get the birth day of a family member
     *
     * @param  no
     * @return int birth day
     */
    public int getBDay(){
        return bDay;
    }

    /**
     * set the image fileName of a family member
     *
     * @param  String fileName
     * @return no
     */
    public void setImage(String fileName){
        profileImage = fileName;
    }

    /**
     * get the image fileName of a family member
     *
     * @param  no
     * @return String fileName
     */
    public String getImage(){
        return profileImage;
    }

    /**
     * return true if a family member is older
     *
     * @param  FamilyMember person
     * @return true if this -- the family member -- is older
     */
    public boolean isOlder(FamilyMember person){
        if(this.bYear > person.getBYear())
            return false;
        else if (this.bYear == person.getBYear() ){
            if(this.bMonth > person.getBMonth()) {
                return false;
            }
            else if(this.bMonth == person.getBMonth()){
                return this.bDay < person.getBDay();
            }
            else
                return true;
        }
        else
            return true;
    }

    /**
     * return a string representation of the family member
     *
     * @param  no
     * @return a string representation of the family member
     */
    public String toString(){
        return firstName + " " + lastName;
    }

    public static void main(String[] args){
        FamilyMember p1 = new FamilyMember("Jon", "Snow", 6, 11, 1988, "male");
        FamilyMember p2 = new FamilyMember("Dany", "Targaryan", 4, 8, 1988, "female");
        FamilyMember p3 = new FamilyMember("Lyanna", "Stark", 4, 10, 1950, "male");
        FamilyMember p4 = new FamilyMember("Rhaegar", "Targaryan", 4, 10, 1950, "male");
        FamilyMember p5 = new FamilyMember("Arya", "Stark", 11, 8, 1999, "female");
        FamilyMember p6 = new FamilyMember("Robb", "Stark", 5, 11, 1988, "female");

        System.out.println("Testing for the constructor");
        System.out.println(p1);System.out.println(p2);System.out.println(p3);
        System.out.println(p4);System.out.println(p5);System.out.println(p6);

        System.out.println("\nTesting for setSpouse and getSpouse");
        p1.setSpouse(p2);
        System.out.println("The spouse of Jon Snow is, expected: Dany Targaryan, actual: " + p1.getSpouse());
        System.out.println("The spouse of Dany Targaryan is, expected: Jon Snow, actual: " + p2.getSpouse());
        System.out.println("The spouse of Arya Stark is, expected: null, actual: " + p5.getSpouse());

        System.out.println("\nTesting for getGender");
        System.out.println("The gender of Rhaegar Targaryan is, expected: male, actual: " + p4.getGender());
        System.out.println("The gender of Lyanna Stark is, expected: female, actual: " + p3.getGender());

        System.out.println("\nTesting for getBDay, getBMonth, getBYear");
        System.out.println("The birthday of Arya Stark is, expected: 11 8 1999. actual: " + p5.getBDay() + " " + p5.getBMonth() + " " + p5.getBYear());
        System.out.println("The birthday of Jon Snow is, expected: 5 11 1988. actual: " + p1.getBDay() + " " + p1.getBMonth() + " " + p1.getBYear());

        System.out.println("\nTesting for setImage and getImage");
        p1.setImage("JonSnow.jpg");
        System.out.println("Image filename for Jon Snow: Expected: JonSnow.jpg. Actual: " + p1.getImage());

        System.out.println("\nTesting for isOlder");
        //same year, different month
        System.out.println("Jon Snow is older than Dany Targaryan: Expected: false. Actual: " + p1.isOlder(p2));
        System.out.println("Dany Targaryan is older than Jon Snow: Expected: true. Actual: " + p2.isOlder(p1));
        //same year, same month, different date
        System.out.println("Jon Snow is older than Robb Stark: Expected: false. Actual: " + p1.isOlder(p6));
        System.out.println("Robb Stark is older than Jon Snow: Expected: true. Actual: " + p6.isOlder(p1));
        //same year, same month, same date
        System.out.println("Lyanna Stark is older than Rhaegar Targaryan: Expected: false. Actual: " + p1.isOlder(p2));
        //different year, different month, different date
        System.out.println("Jon Snow is older than Arya Stark: Expected: true. Actual: " + p1.isOlder(p5));
        System.out.println("Arya Stark is older than Dany Targaryan: Expected: false. Actual: " + p5.isOlder(p2));
    }
}