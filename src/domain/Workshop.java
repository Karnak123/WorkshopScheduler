package domain;

public class Workshop {
    private String id;
    private String name;
    private Speaker speaker;

    public Workshop(String id, String name, Speaker speaker) {
        this.id = id;
        this.name = name;
        this.speaker = speaker;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Speaker getSpeaker() {
        return speaker;
    }

    public String toString() {
        return name;
    }
}