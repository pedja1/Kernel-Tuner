package rs.pedjaapps.kerneltuner.constants;

/**
 * Created by pedja on 17.4.14..
 */
public enum TempUnit
{
    celsius, fahrenheit, kelvin;

    public static TempUnit fromString(String value)
    {
        if(celsius.toString().equals(value))
            return celsius;
        else if(fahrenheit.toString().equals(value))
            return fahrenheit;
        else if(kelvin.toString().equals(value))
            return kelvin;
        return celsius;
    }
}
