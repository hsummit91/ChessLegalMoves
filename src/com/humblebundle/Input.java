package com.humblebundle;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Random;
import java.util.Stack;


public class Input {

	// Pawns - 8
	// Queen - 1
	// King - 1
	// Rook - 2
	// Knight - 2
	// Bishop - 2 // Validation

	private boolean[][] board;

	public Input(){
		board = new boolean[8][8];
		for(int i=0; i<8; i++){
			for(int j=0; j<8; j++){
				board[i][j] = false;
			}
		}
	}

	public void processStack(){

		Stack<String> stack = new Stack<String>();
		FileInputStream fis = null;
		BufferedReader br = null;

		try{
			fis = new FileInputStream("input.txt");

			br = new BufferedReader(new InputStreamReader(fis));

			String line = null;

			while ((line = br.readLine()) != null) {
				System.out.println(line);
				stack.push(line);
			}
		}catch(FileNotFoundException f){
			f.printStackTrace();
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(fis != null){
					fis.close();
				}
				if(br != null){
					br.close();
				}
			}catch(FileNotFoundException f){
				f.printStackTrace();
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}


	public void generateInputFile(){
		FileWriter fw = null;
		BufferedWriter bw = null;
		try {
			File file = new File("input.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			fw = new FileWriter(file.getAbsoluteFile());
			bw = new BufferedWriter(fw);

			Random random = new Random();
			int rColor = random.nextInt(2);

			if(rColor==0){
				bw.write("Next Move:Black\n");
			}else{
				bw.write("Next Move:White\n");
			}

			bw.write(generatePawns("Black"));
			bw.write(generateKings("Black"));
			bw.write(generateQueens("Black"));
			bw.write(generateRooks("Black"));
			bw.write(generateKnights("Black"));
			bw.write(generateBishops("Black"));

			bw.write(generatePawns("White"));
			bw.write(generateKings("White"));
			bw.write(generateQueens("White"));
			bw.write(generateRooks("White"));
			bw.write(generateKnights("White"));
			bw.write(generateBishops("White"));

		}catch(IOException e){
			e.printStackTrace();
		}finally{
			try{
				if(bw != null){
					bw.close();
				}
				if(fw != null){
					fw.close();
				}

			}catch(IOException e){
				e.printStackTrace();
			}
		}
	}

	public String generatePawns(String color){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<8; i++){

			int temp[] = getCoordinates();

			int x = temp[0];
			int y = temp[1];

			if(isVacant(x, y)){
				setBoard(x, y);
				sb.append(color+" Pawn:"+(char)(65+x)+","+y+"\n");
			}
		}
		return sb.toString();
	}

	public String generateKings(String color){
		boolean flag = false;
		StringBuilder sb = new StringBuilder();

		do{
			int temp[] = getCoordinates();

			int x = temp[0];
			int y = temp[1];

			if(isVacant(x, y)){
				flag = true;
				setBoard(x, y);
				sb.append(color+" King:"+(char)(65+x)+","+y+"\n");
			}

		}while(flag == false);
		return sb.toString();
	}

	public String generateRooks(String color){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<2; i++){
			int temp[] = getCoordinates();

			int x = temp[0];
			int y = temp[1];


			if(isVacant(x, y)){
				setBoard(x, y);
				sb.append(color+" Rook:"+(char)(65+x)+","+y+"\n");
			}
		}

		return sb.toString();
	}

	public String generateKnights(String color){
		StringBuilder sb = new StringBuilder();
		for(int i=0; i<2; i++){
			int temp[] = getCoordinates();

			int x = temp[0];
			int y = temp[1];


			if(isVacant(x, y)){
				setBoard(x, y);
				sb.append(color+" Knight:"+(char)(65+x)+","+y+"\n");
			}
		}

		return sb.toString();
	}
	
	// Making sure that both bishops are on different color

	public String generateBishops(String color){
		StringBuilder sb = new StringBuilder();
		int first[] = getCoordinates();
		sb.append(getBishop(color, first));
		boolean firstColorIsBlack = isBlack(first);
		int second[];
		boolean secondColorIsBlack;
		do{
			second = getCoordinates();
			secondColorIsBlack = isBlack(second);
		} while(firstColorIsBlack == secondColorIsBlack);
		sb.append(getBishop(color, second));

		return sb.toString();
	}
	
	public boolean isBlack(int[] input) {
		return (input[0]+input[1]) % 2 == 0;
	}
	
	public String getBishop(String color, int[] pos) {
		StringBuilder sb = new StringBuilder();
		int x = pos[0];
		int y = pos[1];
		if(isVacant(x, y)){
			setBoard(x, y);
			sb.append(color+" Bishop:"+(char)(65+x)+","+y+"\n");
		}
		return sb.toString();
	}
	

	public String generateQueens(String color){
		StringBuilder sb = new StringBuilder();

		int temp[] = getCoordinates();

		int x = temp[0];
		int y = temp[1];


		if(isVacant(x, y)){
			setBoard(x, y);
			sb.append(color+" Queen:"+(char)(65+x)+","+y+"\n");
		}

		return sb.toString();
	}

	public int[] getCoordinates(){
		Random random = new Random();
		int pos[] = new int[2];
		pos[0] = random.nextInt(8);
		pos[1] = random.nextInt(8);
		return pos;
	}

	public boolean isVacant(int x, int y){
		return (board[x][y]==false) ? true : false;
	}

	public void setBoard(int x, int y){
		if(isVacant(x, y)){
			board[x][y] = true;
		}
	}

	public static void main(String[] args) {
		Input in = new Input();
		in.generateInputFile();
		in.processStack();
	}

}
