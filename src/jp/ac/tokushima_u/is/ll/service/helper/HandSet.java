package jp.ac.tokushima_u.is.ll.service.helper;

public enum HandSet{
    CellMobile(1), SmartPhone(2);
    private int value;
    HandSet(int v){
        value = v;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}