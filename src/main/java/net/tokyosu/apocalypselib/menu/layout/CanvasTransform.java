package net.tokyosu.apocalypselib.menu.layout;

/** Uniform, centered canvas transform with no client or UI-framework dependencies. */
public record CanvasTransform(float scale, float left, float top, float translateX, float translateY,
                              int screenWidth, int screenHeight) {
    public static CanvasTransform fit(int screenWidth, int screenHeight, int width, int height,
                                      int originX, int originY, float margin, float minimumScale) {
        if (width <= 0 || height <= 0 || minimumScale <= 0) {
            throw new IllegalArgumentException("Canvas dimensions and minimum scale must be positive");
        }
        float scale = Math.max(minimumScale, Math.min(Math.max(1.0F, screenWidth - margin * 2) / width,
                Math.max(1.0F, screenHeight - margin * 2) / height));
        float left = (screenWidth - width * scale) * 0.5F;
        float top = (screenHeight - height * scale) * 0.5F;
        return new CanvasTransform(scale, left, top, left - originX * scale, top - originY * scale,
                screenWidth, screenHeight);
    }

    public double referenceX(double x) { return (x - translateX) / scale; }
    public double referenceY(double y) { return (y - translateY) / scale; }

    public static int horizontalOverscan(int screenWidth, int screenHeight, int width, int height) {
        if (screenWidth <= 0 || screenHeight <= 0 || width <= 0 || height <= 0) return 0;
        float scale = Math.min(screenWidth / (float) width, screenHeight / (float) height);
        return (int) Math.ceil(Math.max(0.0F, (screenWidth - width * scale) * 0.5F) / scale);
    }
}
