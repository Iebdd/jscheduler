package project.scheduler.Util;

public class SingleToken {
    private final String[] tokens;
    private final boolean check;

    public SingleToken(String[] tokens, boolean check) {
        this.tokens = tokens;
        this.check = check;
    }

    public SingleToken(String token, boolean check) {
        this.tokens = new String[]{token};
        this.check = check;
    }

    public String[] getTokens() {
        return this.tokens;
    }

    public boolean isCheck() {
        return this.check;
    }
}
