package de.flo_aumeier.popularmoviesstage1.Model;

/**
 * Created by Flo on 06.02.2017.
 */

public class Movie {
    private int id;
    private String title;
    private String releaseDate;
    private String urlToPoster;
    private String plot;
    private double rating;

    public Movie(int id, String title, String releaseDate, String urlToPoster, String plot, double rating) {
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.urlToPoster = urlToPoster;
        this.plot = plot;
        this.rating = rating;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Movie movie = (Movie) o;

        if (id != movie.id) return false;
        if (Double.compare(movie.rating, rating) != 0) return false;
        if (!title.equals(movie.title)) return false;
        if (!releaseDate.equals(movie.releaseDate)) return false;
        if (!urlToPoster.equals(movie.urlToPoster)) return false;
        return plot.equals(movie.plot);

    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = id;
        result = 31 * result + title.hashCode();
        result = 31 * result + releaseDate.hashCode();
        result = 31 * result + urlToPoster.hashCode();
        result = 31 * result + plot.hashCode();
        temp = Double.doubleToLongBits(rating);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public String toString() {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", urlToPoster='" + urlToPoster + '\'' +
                ", plot='" + plot + '\'' +
                ", rating=" + rating +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getUrlToPoster() {
        return urlToPoster;
    }

    public void setUrlToPoster(String urlToPoster) {
        this.urlToPoster = urlToPoster;
    }

    public String getPlot() {
        return plot;
    }

    public void setPlot(String plot) {
        this.plot = plot;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
