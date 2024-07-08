package org.example;

class Heart extends BaseLabel {
    public Heart(String file1, int w, int h, MainApplication pf) {
        super(file1, w, h, pf);
    }

    @Override
    public void updateLocation() {
        setLocation(curX, curY);
    }

    public void setHeartPosition(int x, int y) {
        this.curX = x;
        this.curY = y;
        updateLocation();
    }
}
