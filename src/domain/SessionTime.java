package domain;

public class SessionTime {
    private String id;
    private String time;
    private String previousId;

    public SessionTime(String id, String time, String previousId) {
        this.id = id;
        this.time = time;
        this.previousId = previousId;
    }

    public String getId() {
        return id;
    }

    public String getTime() {
        return time;
    }

    public String getPreviousId() {
        return previousId;
    }

    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SessionTime other = (SessionTime) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        return true;
    }
}
