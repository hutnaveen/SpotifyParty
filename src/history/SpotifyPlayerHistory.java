package history;

import model.Track;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class SpotifyPlayerHistory extends ArrayList<Track> {
    private static SpotifyPlayerHistory history = new SpotifyPlayerHistory();
    private SpotifyPlayerHistory()
    {

    }

    @Override
    protected void removeRange(int fromIndex, int toIndex) {

    }

    @Override
    public boolean remove(Object o) {
        return false;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        return false;
    }

    @Override
    public Track remove(int index) {
        return null;
    }

    @Override
    public boolean removeIf(Predicate<? super Track> filter) {
        return false;
    }

    @Override
    public void replaceAll(UnaryOperator<Track> operator) {
    }

    @Override
    public Track set(int index, Track element) {
        return null;
    }

    @Override
    public void add(int index, Track element) {
    }

    @Override
    public boolean addAll(int index, Collection<? extends Track> c) {
        return false;
    }

    public static SpotifyPlayerHistory getHistory() {
        return history;
    }
}
