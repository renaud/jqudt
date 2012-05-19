Java Library to deal with QUDT units and conversions between them.

QUDT is "Quantities, Units, Dimensions and Data Types in OWL and XML"

    http://www.qudt.org/

License: BSD

Quick demo
==========

Source:

    Measurement obs = new Measurement(0.1, Micromolar.getInstance());
    System.out.println(obs + " = " +  obs.convertTo(Nanomolar.getInstance()));
    
    Measurement temp = new Measurement(20, Celsius.getInstance());
    System.out.println(temp + " = " +  temp.convertTo(Kelvin.getInstance()));

Output

    0.1 μM = 100.00000000000001 nM
    20.0 C = 293.0 K

