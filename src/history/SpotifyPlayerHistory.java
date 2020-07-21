package history;

import model.Item;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class SpotifyPlayerHistory extends ArrayList<Item> {
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
    public Item remove(int index) {
        return null;
    }

    @Override
    public boolean removeIf(Predicate<? super Item> filter) {
        return false;
    }

    @Override
    public void replaceAll(UnaryOperator<Item> operator) {
    }

    @Override
    public Item set(int index, Item element) {
        return null;
    }

    @Override
    public void add(int index, Item element) {
    }

    @Override
    public boolean addAll(int index, Collection<? extends Item> c) {
        return false;
    }

    public static SpotifyPlayerHistory getHistory() {
        return history;
    }
}
