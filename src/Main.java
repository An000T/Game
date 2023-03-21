import java.util.Random;
import java.util.Scanner;


public class Main {

    private static Hero player;
    private static Unit enemy;
    private static boolean stop = false;

    public static void main(String[] args) {

        Scanner in = new Scanner(System.in);
        System.out.print("Имя героя: ");
        String name = in.nextLine();
        player = new Hero( name ,100, 100, 25, 0, 25);
        while (stop != true){
            Main.Menu();

        }

    }

    public static void Menu(){

        Scanner in = new Scanner(System.in);
        System.out.print("1. К торговцу\n" +
                "2. В тёмный лес\n" +
                "3. На выход \n");
        int way = in.nextInt();
        if (way == 1) {
            System.out.println("Идем к торговцу");
            Seller seller = new Seller();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            seller.BuyPotion();
        }
        Thread game = new GameThread("d");
        if (way == 2) {
            System.out.println("Идем в лес");
            game.start();

            try{
                game.join();
            }
            catch(InterruptedException e){

                System.out.printf("Ошибка завершения потока", game.getName());
            }
        }
        if (way == 3) {
            stop = true;
            System.out.println("Выход");



        }
    }

    static class Seller {
        public void BuyPotion(){
            Scanner in = new Scanner(System.in);
            System.out.println("Купить лечебное зелье?\n 1.Да \n 2.Нет");
            int answer = in.nextInt();
            if (answer == 1) {
                if (player.gold >= 100) {
                    player.hp = 100;
                    player.gold = player.gold - 100;
                    System.out.println("Здоровье героя: " + player.hp);
                    System.out.println("Текущее золото: " + player.gold);
                }
                else {
                    System.out.println("Недостаточно золота");
                }
                Main.Menu();

            }
            else {
                System.out.println("Уходим");
                Main.Menu();
            }
        }
    }

    static class GameThread extends Thread{



        GameThread(String name){
            super(name);
        }


        @Override
        public void run(){
            Random random = new Random();

            while ((player.hp > 0) && (stop != true)) {
                int unit = random.ints(1, 3).findAny().getAsInt();
                if (unit == 1) {
                    enemy = new Goblin("Goblin", 100, 50, 15, 5, 20);
                    System.out.println("Перед Вами гоблин");
                } else {
                    enemy = new Skeleton("Skeleton", 100, 50, 15, 5, 20);
                    System.out.println("Перед Вами скелет");
                }
                while ((enemy.hp > 0) && (player.hp > 0)) {
                    enemy.hp = enemy.hp - player.attack();
                    System.out.println("Здоровье: " + enemy.name + " " + enemy.hp);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    if (enemy.hp > 0) {
                        player.hp = player.hp - enemy.attack();
                        System.out.println("Здоровье героя: " + player.hp);
                    }
                }
                if (enemy.hp <= 0) {
                    System.out.println("Враг побежден");
                    player.gold = player.gold + enemy.gold;
                    player.exp = player.exp + enemy.exp;
                    System.out.println("Текущее золото: " + player.gold);
                    System.out.println("Текущий опыт: " + player.exp);
                    System.out.print("Далее:\n 1. Продолжить\n 2. Меню\n");
                    Scanner in = new Scanner(System.in);

                    //System.out.println(in.nextInt());
                    if (in.nextInt() == 2) {
                        Main.Menu();
                    }

                }

                if (player.hp <= 0) {
                    System.out.println("Вы мертвы");
                    stop = true;
                }


            }
        }


    }
}
class Unit {
    public String name;
    public int hp;
    public int gold;
    public int dex;
    public int exp;
    public int power;

    public Unit (String name, int hp, int gold, int dex, int exp, int power){
        this.name = name;
        this.hp = hp;
        this.gold = gold;
        this.dex = dex;
        this.exp = exp;
        this.power = power;

    }

    public int attack() {
        Random random = new Random();
        int coef = random.ints(0,101).findAny().getAsInt();
        if ((coef >= 20) && (coef <= 30) && (dex * 3 > coef)) {
            coef = 2;
        }
        else if ((dex * 3 > coef)){
            coef = 1;
        }
        else coef = 0;
        return coef * power;
    }

}
class Hero extends Unit{
    public Hero (String name, int hp,int gold,int dex,int exp,int power){
        super(name, hp, gold, dex, exp, power);
    }
}
class Goblin extends Unit{
    public Goblin (String name, int hp,int gold,int dex,int exp,int power){
        super(name, hp, gold, dex, exp, power);
    }
}
class Skeleton extends Unit{
    public Skeleton (String name, int hp,int gold,int dex,int exp,int power){
        super(name, hp, gold, dex, exp, power);
    }

}
