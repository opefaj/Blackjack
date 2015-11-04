package blackjack;

import java.util.Random;
import java.util.ArrayList;
import java.util.Scanner;

public class Blackjack {
	
	  private Hand playersHand;
	  private Hand dealersHand;
	  private Deck deck;
	  private Scanner scan;

	  public Blackjack() {
	    
	    scan = new Scanner(System.in);
	  }

	  public static void main(String[] args) {
	    
	    Blackjack BlackjackInstance = new Blackjack();
	    BlackjackInstance.run();
	  }

	  public void run() {
	    
	    // deal two cards to player and one to dealer
	    initialise();
	    // request play from player until finished
	    playPlayersHand();
	    // resolve the dealers hand according to the rules
	    playDealersHand();
	    
	    showOutcome();
	  }

	  public void initialise() {
	    
	    System.out.println("Shuffling...");
	    deck = new Deck(new Random());
	    deck.shuffle();
	    playersHand = new Hand();
	    dealersHand = new Hand();
	    Card nextCard;
	    System.out.println("Dealing...");
	    // first card to player
	    deck.dealToHand(playersHand);
	    // second card to dealer
	    deck.dealToHand(dealersHand);
	    // third card to player
	    deck.dealToHand(playersHand);
	  }

	  public void playPlayersHand() {
	    
	    char selection = 'a';
	    while (selection != 's') {
	      showHands();
	      // request next action from player
	      System.out.println();
	      System.out.println("\tWhat would you like to do: ");
	      System.out.println("\t\th - hit");
	      System.out.println("\t\ts - stick");
	      System.out.print("\tEnter selection: ");
	      selection = scan.next().charAt(0);
	      System.out.println();
	      
	      if (selection == 'h') {
	        deck.dealToHand(playersHand);
	        
	        if (playersHand.isBust()) {
	          System.out.println("Your hand is bust!");
	          System.out.println();
	          break;
	        }
	      }
	    }
	    showHands();
	  }

	  public void playDealersHand() {
	     
	    while (!dealersHand.dealerSticks()) {
	      System.out.println("Dealer hits!");
	      deck.dealToHand(dealersHand);
	      showHands();
	    }
	    
	    if (dealersHand.isBust()) {
	      System.out.println("Dealer is bust!");
	    }
	  }

	  public void showOutcome() {
	    
	    System.out.println("Final Outcome...");
	    showHands();

	    if (dealersHand.isBust()) {
	      
	      if (playersHand.isBust())
	        System.out.println("Player and dealer are bust. It's a draw!");
	      else
	        System.out.println("Dealer is bust. Player wins!");

	    } else {
	     
	      if (playersHand.isBust())
	        System.out.println("Player is bust. Dealer wins!");
	      else if (playersHand.getHandValue() > dealersHand.getHandValue())
	        System.out.println("Player wins!");
	      else
	        System.out.println("Dealer wins!");

	    }

	  }

	  public void showHands() {
	   
	    System.out.println("\tDealer's hand: " + dealersHand);
	    System.out.println("\tPlayer's hand: " + playersHand);
	    System.out.println();
	  }

	}

	class Card {

	  public static int HEARTS=0, CLUBS=1, DIAMONDS=2, SPADES=3;
	  private int faceValue; 
	  private int suit;

	  public Card(int faceValue, int suit) {
	   	  this.faceValue = faceValue;
		  this.suit = suit;
	  }

	  public int getFaceValue() {
	    
		  return faceValue;
	  
	  }

	  public int getPointValue() {
	    
		  if(faceValue == 1)
		  {
			  return 11;
		  }
		  
		  else if(faceValue > 10)
		  {
			  return 10;
		  }
		
		  else
		  {
			  return faceValue;
		  }
		  
	    
	  }

	  public int getSuit() {
	    
		  return suit;
	    
	  }

	  public boolean isAce() {
	    
	    return (faceValue == 1);
	  }

	 
	  public static int[] getAllSuits() {
	    
		  
	    return new int [] {HEARTS, CLUBS, DIAMONDS,SPADES};
	  }

	  public static int[] getAllFaceValues() {
	    
	    int[] values = new int[13];
	    for(int faceValue = 1; faceValue <= 13; faceValue++)
	      values[faceValue-1] = faceValue;
	    return values;
	  }

	  public String toString() {
	   
	    return faceValueToString(getFaceValue()) + suitToString(getSuit());
	  }

	  public static String faceValueToString(int faceValue) {
	   
	    switch(faceValue) {
	      case 1: return "A";
	      case 13: return "K";
	      case 12: return "Q";
	      case 11: return "J";
	      default: return String.valueOf(faceValue);
	    }
	  }

	  public static String suitToString(int suit) {
	    
	    switch(suit) {
	      case 0: return "H";
	      case 1: return "C";
	      case 2: return "D";
	      case 3: return "S";
	      default: return "?";
	    }
	  }

	}

	class Hand {

	  public static final int MAXPOINTS = 21;
	  public static final int DEALERSTICKSAT = 17;
	 
	  ArrayList<Card> cards = new ArrayList<Card>();
	 
	  
	  public Hand() {
		  
	  }

	  public ArrayList<Card> getCardsInHand() {
	    
	    return cards;
	  }

	  public void addCard(Card nextCard) {
	    
		  cards.add(nextCard);
	  }

	  public int getHandValue() {
	    
		 int total = 0;
		 int hard_ace = 0;
		     for(int i = 0; i < cards.size(); i++){
		
			  total += cards.get(i).getPointValue();
			 
			 if(cards.get(i).getPointValue() == 11)
			  {
				  hard_ace++;
			  }
		  }
		  //if the total point value is < 21, return the total as nothing else needs to be done
		  if(total < MAXPOINTS)
		  {
			  return total;
		  }
		  
		  else if (total > MAXPOINTS && hard_ace > 0)
		  {
			  
			  while(hard_ace > 0)
			  {
				  
				  total -= 10;
				  
				  if(total < MAXPOINTS)
				  {
					  break;
				  }
				  
				  hard_ace--;
			  }
			  
			  return total;
		  }
		  
		  else
		  {
			  return total;
		  }
	  }

	  public boolean isBust() {
		 
	    
		 return (getHandValue() > MAXPOINTS);
	  }

	  public boolean dealerSticks() {
	    
		  return(getHandValue() >= DEALERSTICKSAT);
		  
	  }

	 
	  public String toString() {
	    
	    String s = "";
	    for (Card card : getCardsInHand()) {
	      s += card + " ";
	    }
	    s += "(point value = " + getHandValue() + ")";
	    return s;
	  }

	}



	class Deck {

	  
	  private Random generator;
	  private Card[] cards;
	  private int topOfDeck;

	  public Deck(Random generator) {
	    
	    this.generator = generator;
	    int pos = 0;
	    
	    this.cards = new Card[52];
	    for(int suit : Card.getAllSuits()) {
	      for(int value : Card.getAllFaceValues()) {
	        this.cards[pos] = new Card(value, suit);
	        pos++;
	      }
	    }
	    this.topOfDeck = 0;
	  }

	  public void stackDeck(Card[] cards) {
	    
	    this.cards = cards;
	    this.topOfDeck = 0;
	  }

	  private Card getTopOfDeck() {
	    
	    return cards[topOfDeck];
	  }

	  public void dealToHand(Hand hand) {
	    
		Card cardToHand = getTopOfDeck();
		hand.addCard(cardToHand);
		topOfDeck++;
	  }

	  
	  public String toString() {
	    
	    String s = "Deck looks like:\n";
	    for (Card card : cards) {
	      s += " " + card;
	    }
	    return s;
	  }

	  public void shuffle() {
	    
	    int newPos;
	 
	    for(int pos=0; pos < 51; pos++) {

	      newPos = generator.nextInt(52-pos) + pos;

	      switchCards(pos, newPos);
	    } 
	  
	    topOfDeck = 0;
	  }

	  
	  private void switchCards(int position1, int position2) {
	  
	    Card temp = cards[position1];
	    cards[position1] = cards[position2];
	    cards[position2] = temp;
	  }

	}

