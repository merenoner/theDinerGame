import java.io.Serializable;

public class GameSave implements Serializable{
    //bu classtan açılacak obje ile oyunda kaydedilmesi gereken her şey alıncak
    //sonra dosyaya yazdırılcak

    //kameranın konumu
    public Camera camera;

    public Player player;
    //main-static player (entities içinde var ama tek başına da çok kullandım kolaylık sağladığından)
    public double playerHealth;
    public int playerScore;

    //dalga boolean değerleri
    public boolean[] dalga;

    //handler
    public Handler handler;

    //hangi turda, turun hangi spawner indisinde kaldık
    public int tour, zombieIndex;

    //silahla alakalı bilgiler
    public GunType gunType;
    //dosyadan okurken type'a göre hızlıca silahı modifiye edeceğiz
    
}
