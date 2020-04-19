import java.util.Comparator;

public class DoubleWeightComperator implements Comparator<Edge>
{
    @Override
    public int compare(Edge x, Edge y)
    {
        if (x.getWeight() < y.getWeight())
        {
            return -1;
        }
        if (x.getWeight() > y.getWeight())
        {
            return 1;
        }
        return 0;
    }

}