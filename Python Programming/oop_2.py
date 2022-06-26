"""
Description: playing a card game called "blackjack", using graphical features for the user interface
"""

import random, time, sys
from cs1graphics import *

suit_name = ["Clubs", "Diamonds", "Hearts", "Spades"]
rank_name = {"2": 2, "3": 3, "4": 4, "5": 5, "6": 6, "7": 7, "8": 8, "9": 9, 
             "10": 10, "Jack": 10, "Queen": 10, "King": 10, "Ace": 11}

class Table:
    def __init__(self):
        self.x0 = 300
        self.depth0 = 100
        
        self.delta_x = 50
        self.delta_d = -5

        self.player_x = []
        self.player_d = []

        self.dealer_y = 80
        self.player_y = 200

        self.message = Text() # 게임 결과 적는 곳
        self.message.setFontColor("red")
        self.message.setFontSize(20)
        self.message.moveTo(canvas.getWidth() / 2 - 50, canvas.getHeight() - 80)
        canvas.add(self.message)

        self.question = Text() # 질문 적는 곳 
        self.question.setFontColor("white")
        self.question.setFontSize(20)
        self.question.moveTo(canvas.getWidth() / 2 - 50, canvas.getHeight() - 40)
        canvas.add(self.question)

        self.PLAYERS = self.set_game()

        for i in range(self.PLAYERS):
            self.player_x.append(self.x0)
            self.player_d.append(self.depth0)

        self.scores = []
        self.labels = []
        self.hands = []

        self.question.setMessage("")
        for i in range(self.PLAYERS):
            if i == self.PLAYERS - 1: # 마지막이 Dealer가 되도록 
                label = "Dealer"
            else:
                label = "Player %1d" % (i + 1)

            if i == self.PLAYERS - 1: # Dealer 카드 위치 
                y = 80
            else: # Player 카드 위치 
                y = 200 + i * 120

            self.display_label(label, y) # Label을 canvas에 적기 
            score = Text() # 점수를 canvas 오른쪽에 적기
            score.setFontColor("white")
            score.setFontSize(20)
            score.moveTo(canvas.getWidth() - 100, y)
            canvas.add(score) 
            self.scores.append(score)
            hand = Hand()
            self.hands.append(hand)

    def set_game(self):
        self.question.setMessage("How many persons? ")
        while True:
            event = canvas.wait()
            if event == None: # 프로그램을 비정상 종료 시켰을 때 
                sys.exit(1)
            response = event.getDescription()
            if response == "keyboard": # response가 keyboard를 친거면
                key = event.getKey() # keyboard 값을 받아와서
                if "1" < key < "5": # 2, 3, 4 중 하나를 입력했을 시 
                    self.question.setMessage("Number of players: " + key) # 이 메세지를 보내기
                    time.sleep(2)
                    key = int(key) # key 값을 integer로 변경하여 return 
                    return key
                else:
                    self.question.setMessage(key + "? Type in a number between 2 and 4, inclusively.") # 그 외의 값을 입력 시 다시 입력하라고 하기 
        
    def display_label(self, label, y):
        player_label = Text()
        player_label.setFontColor("white")
        player_label.setFontSize(20)
        player_label.moveTo(100, y)
        canvas.add(player_label)
        player_label.setMessage(label)
        self.labels.append(player_label)

    def clear_label(self): # Label 지우기
        for label in self.labels:
            label.setMessage("")

    def clear_canvas(self): # canvas 지우기 
        canvas.remove(self.message)
        canvas.remove(self.question)

    def set_score(self, score, who): # 현재 점수 매기기 
        text = "%3d" % score
        self.scores[who].setMessage(text)

    def show_message(self, text): # 메세지 보여주고 3초 기다리기 
        self.message.setMessage(text)
        time.sleep(3)

    def ask_response(self, prompt): # 질문 묻고 그 질문에 대한 답에 대해 반응하기 
        self.question.setMessage(prompt)
        while True:
            event = canvas.wait()
            if event == None:
                sys.exit(1)

            response = event.getDescription()
            if response == "keyboard":
                key = event.getKey()
                if key == 'y':
                    self.question.setMessage("")
                    return True

                if key == 'n':
                    self.question.setMessage("")
                    return False
                else:
                    self.question.setMessage(key + "? I beg your pardon.")

            else:
                self.question.setMessage(key)

    def start_new(self, prompt): # prompt에 넣고 싶은 메세지 쓰기 
        self.question.setMessage(prompt + "(y/n)")
        while True:
            event = canvas.wait()
            response = event.getDescription()
            if response == "canvas close":
                sys.exit(1)

            if response == "keyboard":
                key = event.getKey()
                if key == 'y':
                    self.question.setMessage("")
                    return True

                if key == 'n':
                    self.question.setMessage("")
                    return False
        self.question.setMessage(key + "I beg your pardon.")

    def clear(self): # 다 지우고 초기화 하기 
        for i in range(self.PLAYERS):
            self.player_x[i] = self.x0
            self.player_d[i] = self.depth0
            self.scores[i].setMessage("")
            for image in self.hands[i].images:
                canvas.remove(image)
            self.hands[i].images = []
            self.hands[i] = Hand()

    def show_card(self, card, who):
        if who == self.PLAYERS - 1: # Dealer 카드일 때 
            image = self.display_card(card, self.player_x[self.PLAYERS - 1], self.dealer_y, self.player_d[self.PLAYERS - 1])
        
        else:
            y = self.player_y + who * 120
            image = self.display_card(card, self.player_x[who], y, self.player_d[who])
        self.player_x[who] += self.delta_x
        self.player_d[who] += self.delta_d # depth 추가하기 
        self.hands[who].images.append(image) # 이미지 넣기 

    def open_hcard(self, hand):
        card = hand.cards[0]
        card.update_state(False) # Dealer의 히든 카드 열기 
        image = self.display_card(card, self.x0, self.dealer_y, self.player_d[self.PLAYERS - 1])
        self.hands[self.PLAYERS - 1].images.append(image)

        return card

    def display_card(self, card, x, y, d):
        if card.hidden: # 처음에 Dealer의 히든 카드 안보여주기 
            image = Image("./BlackJack/" + "Back.png")
        else:
            image = card.image
        image.setDepth(d)
        image.moveTo(x, y)
        canvas.add(image)
        time.sleep(1)

        return image

    def close(self):
        canvas.close()

class Card:
    def __init__(self, suit = "Clubs", rank = "2"):
        self.rank = rank
        self.suit = suit
        self.value = rank_name[rank]
        self.image = Image("./BlackJack/" + suit + "_" + rank + ".png")
        self.hidden = False

    def __str__(self):
        article = "a"
        if self.rank in ("8", "Ace"):
            article = "an "

        return article + self.rank + " of" + self.suit

    def update_state(self, hidden):
        self.hidden = hidden

class Deck:
    """A deck of cards."""
    def __init__(self):
        """Create a deck of 52 cards and shuffle them."""
        self.cards = []
        for suit in suit_name:
            for rank in rank_name:
                card = Card(suit, rank)
                self.cards.append(card)

    def shuffle(self):
        random.shuffle(self.cards)
        return(self)

    def draw(self):
        card = self.cards.pop()
        return card

    def move_card(self, hand, hidden = False):
        card = self.draw()
        hand.add(card, hidden)
        return card

class Hand(Deck):
    def __init__(self):
        self.cards = []
        self.images = []

    def add(self, card, hidden):
        if hidden:
            card.update_state(hidden)
        self.cards.append(card)

    def hand_value(self):
        value = 0
        for card in self.cards:
            value += card.value

        return value

    def clear(self):
        for item in self.cards:
            self.cards.remove(item)


def first_two_cards(deck, table): # Dealer와 Player에게 각자 2장씩 나눠주기 
    for j in range(2):
        for i in range(table.PLAYERS):
            hand = table.hands[i]
            if j == 0 and i == table.PLAYERS - 1: # 히든 카드 주기 
                card = deck.move_card(table.hands[i], True)
            else:
                card = deck.move_card(table.hands[i])

            table.show_card(card, i)

def players_turn(deck, table):
    tval = [0] * table.PLAYERS 
    all_lost = True
    for i in range(table.PLAYERS - 1):
        tval[i] = table.hands[i].hand_value() # 각 Player와 Dealer의 점수 넣어두기 
        table.set_score(tval[i], i)
        while tval[i] < 21: # 점수가 21 안 넘었을 때는 다른 카드 또 받겠냐고 물어보기 
            msg = "Player %1d, " % (i + 1) + "would you like to have another card? (y/n)"
            if not table.ask_response(msg):
                break

            card = deck.move_card(table.hands[i])
            table.show_card(card, i)
            tval[i] += card.value
            table.set_score(tval[i], i)

        if tval[i] > 21: # 점수가 21 넘으면 졌다고 보여주기 
            msg = "Player %1d, " % (i + 1) + "you went over 21! You lost!"
            table.show_message(msg)
            msg = ""
            table.show_message(msg)

        else:
            all_lost = False 

        time.sleep(1)
    return tval, all_lost

def dealers_turn(deck, tval, table):
    card = table.open_hcard(table.hands[table.PLAYERS - 1]) # 히든 카드 보여주기 
    tval[table.PLAYERS - 1] = table.hands[table.PLAYERS - 1].hand_value()
    table.set_score(tval[table.PLAYERS - 1], table.PLAYERS - 1)
    while tval[table.PLAYERS - 1] < 17 and tval[table.PLAYERS - 1] <= max(tval[0 : table.PLAYERS - 1]): # 17보다 낮거나 Player의 카드보다 점수가 작을 때는 카드 더 받기
        card = deck.move_card(table.hands[table.PLAYERS - 1])
        table.show_card(card, table.PLAYERS - 1)
        tval[table.PLAYERS - 1] += card.value 
        table.set_score(tval[table.PLAYERS - 1], table.PLAYERS - 1)

    return tval

def conclude_the_game(tval, table):
    for i in range(table.PLAYERS - 1):
        if tval[i] > 21:
            msg = "you went over 21! You lost!"
        elif tval[table.PLAYERS - 1] > 21: # Dealer 점수가 21 넘어가서 
            msg = "the dealer went over 21! You won!"
        elif tval[table.PLAYERS - 1] < tval[i]:
            msg = "you won!"
        elif tval[table.PLAYERS - 1] > tval[i]:
            msg = "you lost!"
        else:
            msg = "You had a tie!"

        msg = "Player %1d, " % (i + 1) + msg # 각 Player들에게 게임 결과 말해주기 
        table.show_message(msg)
        time.sleep(1)
        msg = ""
        table.show_message(msg)

    table.clear()

def blackjack(table):
    deck = Deck()
    deck.shuffle() # 카드 섞기 
    first_two_cards(deck, table)
    tval, all_lost = players_turn(deck, table)
    if all_lost: # 모든 Player가 점수를 21 넘어갔을 때
        table.clear()
        return

    tval = dealers_turn(deck, tval, table)
    conclude_the_game(tval, table)

def game_loop():
    table = Table()
    while True:
        blackjack(table)
        if not table.ask_response("Play another round? (y/n) "):
            table.clear_label() # 다른 round 안한다고 했으니까 이제 Player 지우기 
            if table.start_new("Start with new persons?"): # 다른 round를 안한다고 했을 때 그럼 다시 새로운 사람과 할래 하고 묻기
                table.clear_canvas() # 새로운 사람과 게임하겠다고 하면 canvas 지우고 다시 table 시작하기
                table = Table()

            else:
                break # 새로운 사람과도 안하겠다면 break하여 while문 나오기 
            
    table.close() # table 닫기 

canvas = Canvas(1000, 600, 'dark green', 'Black Jack')

game_loop()