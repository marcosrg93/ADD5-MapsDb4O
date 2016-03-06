package com.rubino.mapsdb4o.pojo;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by marco on 05/03/2016.
 */
public class Posicion  implements Parcelable{

    private float Latitud;
    private float Longitud;
    private int dia;

    public Posicion() {
    }

    public Posicion(float latitud, float longitud) {
        Latitud = latitud;
        Longitud = longitud;
        this.dia = 0;
    }

    public Posicion(float latitud, float longitud, int dia) {
        Latitud = latitud;
        Longitud = longitud;
        this.dia = dia;
    }

    protected Posicion(Parcel in) {
        Latitud = in.readFloat();
        Longitud = in.readFloat();
        dia = in.readInt();
    }

    public static final Creator<Posicion> CREATOR = new Creator<Posicion>() {
        @Override
        public Posicion createFromParcel(Parcel in) {
            return new Posicion(in);
        }

        @Override
        public Posicion[] newArray(int size) {
            return new Posicion[size];
        }
    };

    public float getLatitud() {
        return Latitud;
    }

    public void setLatitud(float latitud) {
        Latitud = latitud;
    }

    public float getLongitud() {
        return Longitud;
    }

    public void setLongitud(float longitud) {
        Longitud = longitud;
    }

    public int getDia() {
        return dia;
    }

    public void setDia(int dia) {
        this.dia = dia;
    }

    @Override
    public String toString() {
        return "Posicion{" +
                "Latitud=" + Latitud +
                ", Longitud=" + Longitud +
                ", dia=" + dia +
                '}';
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeFloat(Latitud);
        dest.writeFloat(Longitud);
        dest.writeInt(dia);
    }
}
