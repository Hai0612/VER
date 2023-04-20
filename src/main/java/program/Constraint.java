package program;

public class Constraint {
    private String eventA;
    private String eventB;
    private String constraint;
    public String getEventA() {
        return eventA;
    }

    public String getConstraint() {
        return constraint;
    }
    public Constraint(String eventA,  String constraint,String eventB){
        this.eventA = eventA;
        this.eventB = eventB;
        this.constraint = constraint;
    }
    public void setConstraint(String constraint) {
        this.constraint = constraint;
    }

    public void setEventA(String eventA) {
        this.eventA = eventA;
    }

    public String getEventB() {
        return eventB;
    }

    public void setEventB(String eventB) {
        this.eventB = eventB;
    }

}
