package presentation;

import java.util.InputMismatchException;
import java.util.Scanner;

public class LeitorEntrada {

    private final Scanner sc = new Scanner(System.in);

    public int lerInteiro(){
        while (true){
            try {
                int valor = sc.nextInt();
                sc.nextLine();
                return valor;
            }catch (InputMismatchException e){
                System.out.println("Digite um número inteiro.");
                sc.nextLine();
            }
        }
    }

    public double lerDouble(){
        while (true){
            try {
                double valor =  sc.nextDouble();
                sc.nextLine();
                return valor;
            }catch (InputMismatchException e){
                System.out.println("Digite um numero real.");
                sc.nextLine();
            }
        }
    }

    public String lerString(){
        while (true){
            try {
                String texto = sc.nextLine();
                if (!texto.trim().isEmpty()){
                    return texto;
                }
            }catch (InputMismatchException e){
                System.out.println("Texto invalido, tente novamente.");
            }
        }
    }

    public void fechar(){
        sc.close();
    }
}
