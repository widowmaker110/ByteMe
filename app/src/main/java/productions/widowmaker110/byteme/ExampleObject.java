package productions.widowmaker110.byteme;

/**
 * Created by Widowmaker110 on 11/20/2015.
 *
 * This object class is meant to mimick possible data types held within a single object
 *
 * For simplicity, I will be mimicking a simplified user profile data
 */
public class ExampleObject {

    private String Name;
    private int Age;
    private String Location;
    private String Sex;
    private String Description;

    /**
     * Empty Constructor
     */
    public ExampleObject() {}

    /**
     * Basic constructor with initializing data
     *
     * @param _Name String with the name of the user
     * @param _Age Integer with the age of the user
     * @param _Location String containing the curret city and state of the user
     * @param _Sex String Male, Female, Transgender, or Other
     * @param _Description String short blurb about the user
     */
    public ExampleObject(String _Name, int _Age, String _Location, String _Sex, String _Description)
    {
        this.setName(_Name);
        this.setAge(_Age);
        this.setLocation(_Location);
        this.setSex(_Sex);
        this.setDescription(_Description);
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getAge() {
        return Age;
    }

    public void setAge(int age) {
        Age = age;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getSex() {
        return Sex;
    }

    public void setSex(String sex) {
        Sex = sex;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }
}
