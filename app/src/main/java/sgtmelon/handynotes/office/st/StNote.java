package sgtmelon.handynotes.office.st;

public class StNote {

    private boolean create;
    private boolean edit = true;
    private boolean bin = false;

    public boolean isCreate() {
        return create;
    }

    public void setCreate(boolean create) {
        this.create = create;
    }

    public boolean isEdit() {
        return edit;
    }

    public void setEdit(boolean edit) {
        this.edit = edit;
    }

    public void setEdit() {
        edit = create;
    }

    public boolean isBin() {
        return bin;
    }

    public void setBin(boolean bin) {
        this.bin = bin;
    }
}