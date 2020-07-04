package queue;

import model.Track;

import java.util.LinkedList;

public class SpotifyPlayerQueue extends LinkedList<Track> {
    private boolean active;
    private static SpotifyPlayerQueue queue = new SpotifyPlayerQueue();
    private SpotifyPlayerQueue()
    {
        super();
        active = false;
    }
    public void addToBack(Track song)
    {
        addLast(song);
    }
    public void addToFront(Track song)
    {
        addFirst(song);
    }
    public Track popFront()
    {
        return removeFirst();
    }
    public Track popBack()
    {
        return removeLast();
    }
    public Track seekFront()
    {
        return getFirst();
    }
    public Track seekBack()
    {
        return getLast();
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
    public static SpotifyPlayerQueue getQueue() {
        return queue;
    }
}
