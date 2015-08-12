#!/usr/bin/env ruby

EXAMPLE_TO_THE_RIVER =<<__EXAMPLE__
PokerStars Hand #137309662962:  Hold'em No Limit ($1/$2 USD) - 2015/06/27 22:47:51 ET
Table 'Medusa' 9-max Seat #9 is the button
Seat 1: EsKoTeiRo ($200 in chips)
Seat 3: butcheN18 ($253.33 in chips)
Seat 4: Icantoo61 ($119.56 in chips)
Seat 5: RUS)Timur ($200 in chips)
Seat 6: YaDaDaMeeN21 ($240.20 in chips)
Seat 7: tarlardon1 ($189 in chips)
Seat 8: CI58 ($244.27 in chips)
Seat 9: FERA PB ($118.91 in chips)
EsKoTeiRo: posts small blind $1
butcheN18: posts big blind $2
*** HOLE CARDS ***
Icantoo61: folds
RUS)Timur: raises $2.36 to $4.36
YaDaDaMeeN21: folds
tarlardon1: calls $4.36
CI58: folds
FERA PB: calls $4.36
EsKoTeiRo: folds
butcheN18: folds
*** FLOP *** [Ks 4s 2c]
RUS)Timur: bets $7.37
tarlardon1: raises $7.37 to $14.74
FERA PB: folds
RUS)Timur: calls $7.37
*** TURN *** [Ks 4s 2c] [9d]
RUS)Timur: checks
tarlardon1: bets $32
RUS)Timur: calls $32
*** RIVER *** [Ks 4s 2c 9d] [Qd]
RUS)Timur: checks
tarlardon1: checks
*** SHOW DOWN ***
RUS)Timur: shows [Kd Jc] (a pair of Kings)
tarlardon1: mucks hand
RUS)Timur collected $106.76 from pot
*** SUMMARY ***
Total pot $109.56 | Rake $2.80
Board [Ks 4s 2c 9d Qd]
Seat 1: EsKoTeiRo (small blind) folded before Flop
Seat 3: butcheN18 (big blind) folded before Flop
Seat 4: Icantoo61 folded before Flop (didn't bet)
Seat 5: RUS)Timur showed [Kd Jc] and won ($106.76) with a pair of Kings
Seat 6: YaDaDaMeeN21 folded before Flop (didn't bet)
Seat 7: tarlardon1 mucked
Seat 8: CI58 folded before Flop (didn't bet)
Seat 9: FERA PB (button) folded on the Flop
__EXAMPLE__

EXAMPLE_PREFLOP =<<__EXAMPLE__
PokerStars Hand #137309721600:  Hold'em No Limit ($1/$2 USD) - 2015/06/27 22:50:57 ET
Table 'Medusa' 9-max Seat #3 is the button
Seat 1: EsKoTeiRo ($200 in chips)
Seat 3: butcheN18 ($250.33 in chips)
Seat 5: RUS)Timur ($301.94 in chips)
Seat 6: YaDaDaMeeN21 ($240.20 in chips)
Seat 7: tarlardon1 ($89.82 in chips)
Seat 8: CI58 ($244.27 in chips)
Seat 9: FERA PB ($114.55 in chips)
RUS)Timur: posts small blind $1
YaDaDaMeeN21: posts big blind $2
*** HOLE CARDS ***
tarlardon1: folds
CI58: folds
FERA PB: folds
EsKoTeiRo: raises $4 to $6
butcheN18: folds
RUS)Timur: folds
YaDaDaMeeN21: folds
Uncalled bet ($4) returned to EsKoTeiRo
EsKoTeiRo collected $5 from pot
EsKoTeiRo: doesn't show hand
*** SUMMARY ***
Total pot $5 | Rake $0
Seat 1: EsKoTeiRo collected ($5)
Seat 3: butcheN18 (button) folded before Flop (didn't bet)
Seat 5: RUS)Timur (small blind) folded before Flop
Seat 6: YaDaDaMeeN21 (big blind) folded before Flop
Seat 7: tarlardon1 folded before Flop (didn't bet)
Seat 8: CI58 folded before Flop (didn't bet)
Seat 9: FERA PB folded before Flop (didn't bet)
__EXAMPLE__

EXAMPLE_PREFLOP_2 = <<__EXAMPLE__
PokerStars Hand #137309633028:  Hold'em No Limit ($1/$2 USD) - 2015/06/27 22:46:16 ET
Table 'Medusa' 9-max Seat #7 is the button
Seat 3: butcheN18 ($253.33 in chips)
Seat 4: Icantoo61 ($116.56 in chips)
Seat 5: RUS)Timur ($200 in chips)
Seat 6: YaDaDaMeeN21 ($240.20 in chips)
Seat 7: tarlardon1 ($195 in chips)
Seat 8: CI58 ($236.95 in chips)
Seat 9: FERA PB ($121.91 in chips)
CI58: posts small blind $1
FERA PB: posts big blind $2
EsKoTeiRo: sits out
*** HOLE CARDS ***
butcheN18: folds
Icantoo61: raises $4 to $6
RUS)Timur: folds
YaDaDaMeeN21: folds
tarlardon1: folds
CI58: folds
FERA PB: folds
Uncalled bet ($4) returned to Icantoo61
Icantoo61 collected $5 from pot
Icantoo61: doesn't show hand
*** SUMMARY ***
Total pot $5 | Rake $0
Seat 3: butcheN18 folded before Flop (didn't bet)
Seat 4: Icantoo61 collected ($5)
Seat 5: RUS)Timur folded before Flop (didn't bet)
Seat 6: YaDaDaMeeN21 folded before Flop (didn't bet)
Seat 7: tarlardon1 (button) folded before Flop (didn't bet)
Seat 8: CI58 (small blind) folded before Flop
Seat 9: FERA PB (big blind) folded before Flop
__EXAMPLE__

require 'money'
require 'date'
require 'time'
require 'set'
require 'csv'

class IllegalArgumentException < Exception
end

class Player
  attr_reader :screen_name, :initial_stack
  def initialize(screen_name, initial_stack)
    @screen_name   = screen_name
    @initial_stack = initial_stack # ChipAmount
        
    @proxies = [
      PlayerActions.new(self)
    ]
  end
  
  private
    attr_reader :proxies
    
    def method_missing(method_name, *args, &block)
      matching_proxy = proxies.select do |proxy|
        proxy.respond_to?(method_name)
      end
      
      case matching_proxy.size
      when 0
        super(method_name, *args, &block)
      when 1
        matching_proxy.first.send(method_name, *args, &block)
      else
        raise ArgumentError, "multiple proxies respond to `#{method_name}`: #{matching_proxy.join(',')}"
      end
    end
end

class PlayerActions # TODO indicate this is a proxy (either by inheritance or class name)
  attr_reader :player
  def initialize(player)
    @player = player
  end
  
  def fold(street)
    Action.new(player, Action::Fold.new, street)
  end
  
  def bet(amount, street)
    Action.new(player, Action::Bet.new(amount), street)
  end
  
  def call(amount, street)
    Action.new(player, Action::Call.new(amount), street)
  end
  
  def raise(from, to, street)
    Action.new(player, Action::Raise.new(from, to), street)
  end
  
  def check(street)
    Action.new(player, Action::Check.new, street)
  end
  
  def collect(amount, street)
    Action.new(player, Action::Collect.new(amount), street)
  end
  
  def muck(street)
    Action.new(player, Action::Muck.new, street)
  end
  
  def show(cards, street) # TODO parse explanation of hand, e.g. '(a pair of Kings)'
    Action.new(player, Action::Show.new(cards), street)
  end
  
  def sit_out(street)
    Action.new(player, Action::SitOut.new, street)
  end
end

class Action
  attr_reader :player, :action, :street
  def initialize(player, action, street)
    @player = player
    @action = action
    @street = street
  end
  
  def to_string
    "<#{street}: '#{player.screen_name}' #{action.class} (#{action.inspect})>"
  end
  
  class Fold
  end
  
  class Muck
  end
  
  class SitOut
  end

  class Bet
    attr_reader :amount
    def initialize(amount)
      @amount = amount
    end
  end

  class Call
    attr_reader :amount
    def initialize(amount)
      @amount = amount
    end
  end

  class Raise
    attr_reader :from, :to
    def initialize(from, to)
      @from = from
      @to   = to
    end
  end
  
  class Check
  end
  
  class Collect
    attr_reader :amount
    def initialize(amount)
      @amount = amount
    end
  end
  
  class Show
    attr_reader :cards
    def initialize(cards)
      @cards = cards
    end
  end
end

class Seat
  module Role
    Button     = :button
    BigBlind   = :big_blind
    SmallBlind = :small_blind
    # TODO CutOff, UTG, HiJack, LoJack
  end
  
  attr_reader :number, :player
  attr_accessor :role
  
  def initialize(number, player)
    @number = number
    @player = player
  end
end

class HandHistory
  attr_reader :seat_assignments, :actions, :hand_id, :game_type, :blinds, :started_at
  
  attr_accessor :button, :table, :big_blind, :small_blind, :total_pot, :rake
  attr_accessor :flop, :turn, :river
  def initialize(hand_id, game_type, blinds, started_at)
    @hand_id     = hand_id # #137310594094
    @game_type   = game_type
    @blinds      = blinds
    @big_blind   = nil
    @small_blind = nil
    @started_at  = started_at
    
    @sitting_out = [] # <Player, ...>
    @table       = nil # Table
    @started_at  = nil # DateTime
    
    @total_pot   = nil # $XX.XX
    @rake        = nil # $X.XX
    
    @flop        = nil # <Card, Card, Card>
    @turn        = nil # Card
    @river       = nil # Card
    
    @seat_assignments = SeatAssignments.new([])
    
    @actions     = []
  end
  
  def board
    [flop, turn, river].flatten
  end
  
  def winners
    actions.select {|a| a.action.is_a?(Action::Collect) }
  end
end

class SeatAssignments
  attr_reader :seats
  
  attr_accessor :on_the_button
  
  def initialize(seats = [])
    @seats = seats
    @by_seat_number = {}
    @by_screen_name = {}
  end
  
  def button
    @button ||= begin
      seat = self[on_the_button]
      seat.role = Seat::Role::Button
      seat
    end
  end
  
  def big_blind
    @big_blind ||= player_with_role(Seat::Role::BigBlind)
  end
  
  def small_blind
    @small_blind ||= player_with_role(Seat::Role::SmallBlind)
  end
  
  def [](index)
    case index
    when Fixnum
      by_seat_number[index] ||= (seats.detect do |seat|
        seat.number == index
      end)
    when String
      by_screen_name[index] ||= (seats.detect do |seat|
        seat.player.screen_name == index
      end)
    else
      raise IllegalArgumentError, "unknown seat assigment: `#{index}`"
    end
  end
  
  private
    attr_reader :by_seat_number, :by_screen_name
    
    def player_with_role(role)
      seats.detect do |seat|
        seat.role == role
      end
    end
end

class HandParser
  attr_reader :hand_history
  attr_accessor :current_street
  
  class << self
    def parse(lines)
      parser = new
      lines.each do |line|
        parser.parse_line(line)
      end
      
      parser.hand_history
    end
    
    def from_file(path)
      $hands = nil
      $hands_by_line = []
      hands = IO.read(path).split(/^\s*(?:\r\n)*$/m) # TODO stream file
      $hands = hands.dup
      hands.map do |hand|
        $stderr.puts "\n** NEW HAND **\n" # TODO remove
        lines = hand.strip.split(/(?:\r|\n)+/m).map(&:strip)
        $hands_by_line << lines
        parse(lines)
      end
    end
  end
  
  def initialize
    @hand_over        = false
    @reached_showdown = false
    @seats_assigned   = false
    @current_street   = Street::PreFlop
    @hand_history     = nil
  end
  
  def seats_assigned?
    @seats_assigned == true
  end
  
  def parse_line(line)
    line.chomp!
    
    return if line[/^\s*$/]
    
    case line
    when /\*\*\* HOLE CARDS \*\*\*/
      @seats_assigned = true
    when /\*\*\* SHOW DOWN \*\*\*/
      @reached_showdown = true
    when /\*\*\* SUMMARY \*\*\*/
      @hand_over = true
    else
      @was_parsed = false
      HandLineParser.parse(line) do |hand_id, game_type, stakes, date|
        @was_parsed = true
        @hand_history = HandHistory.new(hand_id, game_type, stakes, date)
      end
      
      SeatAssignmentLineParser.parse(line) do |seat|
        @was_parsed = true
        unless hand_history.seat_assignments.seats.include?(seat)
          hand_history.seat_assignments.seats << seat
        end
      end

      TableLineParser.parse(line) do |name, capacity, button|
        @was_parsed = true
        hand_history.table = Table.new(name, capacity)
        hand_history.seat_assignments.on_the_button = button
      end
      
      BlindPostingLineParser.parse(line) do |user_name, role, amount|
        @was_parsed = true
        hand_history.seat_assignments[user_name].role = role
        
        case role
        when Seat::Role::BigBlind
          hand_history.big_blind = amount
        when Seat::Role::SmallBlind
          hand_history.small_blind = amount
        end          
      end
      
      PlayerCollectedLineParser.parse(line) do |user_name, chip_amount|
        @was_parsed = true
        seat = hand_history.seat_assignments[user_name]
        hand_history.actions << seat.player.collect(chip_amount, current_street)
      end
      
      if seats_assigned?
        seat_assignments = hand_history.seat_assignments
        PlayerActionLineParser.parse(line, seat_assignments) do |seat, action_name, action_string|
          @was_parsed = true
          player = seat.player
          
          action_name = action_name == "doesn't" ? :mucks : action_name.to_sym # TODO unkludge
          
          action = case action_name
            when :folds
              player.fold(current_street)
            when :raises
              match = action_string.match(/(?<from>\S+)\s+to\s+(?<to>\S+)/)
              
              player.raise(
                ChipAmount.from_string(match[:from]), 
                ChipAmount.from_string(match[:to]),
                current_street
              )
            when :calls
              amount = ChipAmount.from_string(action_string[/(\S+)/])
              
              player.call(amount, current_street)
            when :checks
              player.check(current_street)
            when :bets
              amount = ChipAmount.from_string(action_string[/(\S+)/])
              
              player.bet(amount, current_street)
            when :mucks
              player.muck(current_street)
            when :shows
              cards = action_string[/\[([^\]]+)\]/, 1].split(" ").map do |short_hand|
                CardShorthandTranslater.translate(short_hand)
              end
              
              player.show(cards, current_street)
            when :sits
              return
              player.sit_out(current_street)
            when :posts
              # TODO handle
              return
            else
              p action_string
              raise IllegalArgumentException, "unrecognized player action: `#{action_name}`"
            end
          
          hand_history.actions << action
        end
      end
      
      PlayerJoinsLineParser.parse(line) do |user_name, seat_number|
        @was_parsed = true
      end
      
      StreetLineParser.parse(line) do |street, board, next_card|
        @was_parsed = true
        self.current_street = street

        case street
        when Street::Flop
          hand_history.flop = board
        when Street::Turn
          hand_history.turn = next_card
        when Street::River
          hand_history.river = next_card
        end
      end
      
      TotalPotSummaryLineParser.parse(line) do |total_pot, rake|
        @was_parsed = true
        hand_history.total_pot = total_pot
        hand_history.rake = rake
      end
      
      if @was_parsed == false
        # puts "Unparsed: "
        $stderr.puts line.inspect
      else
        @was_parsed = false
      end
    end
  rescue Exception => e
    $stderr.puts "\nEXCEPTION\n"
    $stderr.puts line.inspect
    $stderr.puts
    
    raise e
  end
  
  private
    attr_reader :current_street
end

class LineParser # TODO convert all line parsers to inheret from this
  class << self
    attr_accessor :pattern
    
    def parse(line, &block)
      raise IllegalStateException if pattern.nil?
      if match_data = pattern.match(line)
        block.call(match_data)
      end
    end
  end
end

# e.g.
#
#  pardama collected $37 from pot
class PlayerCollectedLineParser < LineParser
  self.pattern = /^
                  (?<user_name>\S+)
                  \s+collected\s+
                  (?<amount>\S+)
                  \s+from\s+pot$
                  /x
  
  def self.parse(line, &block)
    super(line) do |match|
      block.call(
        match[:user_name],
        ChipAmount.from_string(match[:amount])
      )
    end
  end
end

class PlayerActionLineParser < LineParser
  def self.parse(line, seat_assignments, &block)
    self.pattern = begin
      user_names = seat_assignments.seats.map do |seat|
        Regexp.escape(seat.player.screen_name)
      end
      
      /^
       (?<user_name>#{user_names.join('|')})
       :\s+
       (?<action>\S+)
       (?:\s+
       (?<action_string>.+)
       )?
      $/x
    end

    super(line) do |match|
      block.call(
        seat_assignments[match[:user_name]],
        match[:action],
        match[:action_string]
      )
    end
  end
end

# e.g.
#
#  jackmonoloye will be allowed to play after the button
class NewPlayerNearTheButtonLineParser < LineParser
end

# e.g.
#
#  jackmonoloye joins the table at seat #9
class PlayerJoinsLineParser < LineParser
  self.pattern = /^
                  (?<user_name>\S+)
                  \s+joins\s+the\s+table\s+at\s+seat\s\#
                  (?<seat_number>\d+)
                 /x
                 
  def self.parse(line, &block)
    super(line) do |match|
      block.call(match[:user_name], match[:seat_number])
    end
  end
end

# e.g.
#                   [hand_id     ]  [game_type     ] [stakes   ]   [date                ]
#   PokerStars Hand #137352938919:  Hold'em No Limit ($1/$2 USD) - 2015/06/28 19:30:02 ET
class HandLineParser < LineParser
  self.pattern = /\#
                 (?<hand_id>\w+)
                 :\s+
                 (?<game_type>[^(]+)
                 \s+\(
                 (?<stakes>[^)]+)
                 \)\s+-\s+
                 (?<date>.+)
                 $/x
  class << self
    def parse(line, &block)
      super(line) do |match|
        block.call(
          match[:hand_id], 
          match[:game_type], 
          match[:stakes], 
          DateTime.parse(match[:date])
        )
      end
    end
  end
end

class Table
  def initialize(name, capacity)
    @name        = name # string, e.g. Medusa
    @capacity    = capacity # number, e.g. 9
    @small_blind = nil # $X
    @big_blind   = nil # $X
  end
end

# e.g.
#
#   Table 'Medusa' 9-max Seat #2 is the button
class TableLineParser < LineParser
  self.pattern = /Table\s+'
                  (?<table_name>[^']+)
                  '\s+
                  (?<capacity>\d+)
                  -max\s+Seat\s+\#
                  (?<button>\d+)
                  \s+is
                  /x
                  
  def self.parse(line, &block)
    super(line) do |match|
      block.call(
        match[:table_name],
        Integer(match[:capacity]),
        Integer(match[:button])
      )
    end
  end
end

# e.g.
#
#   Seat 6: YaDaDaMeeN21 ($240.20 in chips)
class SeatAssignmentLineParser < LineParser
  self.pattern = /^Seat\s+
                  (?<seat_number>\d+)
                  :\s+
                  (?<user_name>.+)
                  \s+\(
                  (?<stack_size>[^ ]+)
                  \s+in\s+chips\)
                 $/x
  class << self
    def parse(line, &block)
      super(line) do |match|
        block.call(
          Seat.new(
            Integer(match[:seat_number]), 
            Player.new(
              match[:user_name], 
              ChipAmount.from_string(match[:stack_size])
            )
          )
        )
      end
    end
  end
end

# e.g.
#
#   EsKoTeiRo: posts small blind $1
#   butcheN18: posts big blind $2
class BlindPostingLineParser < LineParser
  SizeToRole = {
    'small' => Seat::Role::SmallBlind,
    'big'   => Seat::Role::BigBlind
  }
  
  SizeToRole.default_proc = ->(h,k) { 
    raise IllegalStateException, "invalid blind size: `#{k}`"
  }
  
  self.pattern = /^
                  (?<user_name>[^:]+)
                  :\s+posts\s+
                  (?<small_or_big>small|big)
                  \s+blind\s+
                  (?<amount>[^ ]+)
                  /x
                  
  def self.parse(line, &block)
    super(line) do |match|
      block.call(
        match[:user_name], 
        SizeToRole[match[:small_or_big]], 
        ChipAmount.from_string(match[:amount])
      )
    end
  end
end

# e.g.
#
#   *** FLOP *** [Ks 4s 2c]
#   *** TURN *** [Ks 4s 2c] [9d]
#   *** RIVER *** [Ks 4s 2c 9d] [Qd]
class StreetLineParser < LineParser
  self.pattern = /\*\*\*\s+
                  (?<street>[A-Z]+)
                  \s+\*\*\*\s+\[
                  (?<board>[^\]]+)
                  \](?:\s+\[
                  (?<next_card>[^\]]+)
                  \])?
                 /x
                 
  def self.parse(line, &block)
    super(line) do |match|
      board = match[:board].split(' ').map do |short_hand|
        CardShorthandTranslater.translate(short_hand)
      end
      
      block.call(
        Street.from_string(match[:street]),
        board,
        CardShorthandTranslater.translate(match[:next_card])
      )
    end
  end
end

# e.g.
#
#    Uncalled bet ($127.40) returned to jackmonoloye
class UncalledBetLineParser
end

# e.g.
#
#   Total pot $10 | Rake $0.45
class TotalPotSummaryLineParser < LineParser
  self.pattern = /^Total\s+pot\s+
                  (?<total_pot>\S+)
                  \s+\|\s+Rake\s+
                  (?<rake>\S+)
                  $/x
  
  def self.parse(line, &block)
    super(line) do |match|
      block.call(
        ChipAmount.from_string(match[:total_pot]),
        ChipAmount.from_string(match[:rake])
      )
    end
  end
end

class Card
  attr_reader :rank, :suit
  def initialize(rank, suit)
    @rank = rank
    @suit = suit
  end
end

# e.g.
#
#   Seat X: XXX  showed [Ts Th] and won ($XX.XX) with two pair, Tens and Deuces
class SeatOutcomeLineParser
  class ShowedLine
    def outcome
      case line
      when /and won/
      when /and lost/
      end
    end
  end
  
  class MuckedLine
  end
  
  class FoldedLine
  end
  
  class CollectedLine
  end
  
  class ShowdownCardsParser
    def parse
      cards_segment = line[/ with (.+)$/, 1]
    end
  end
end

BEGIN {
  class ChipAmount
    attr_reader :amount
  
    CurrencySymbolToCode = {
      '$' => 'USD',
      'Y' => 'YEN',
      '€' => 'EUR',
      'P' => 'POUND'
    }
  
    class << self
      # In the format $XXX or $XXX.XX
      def from_string(string)
        if string =~ /^([^0-9]+)([0-9.]+)/      
          currency_symbol = $1
          amount          = $2
      
          new(
            Money.from_amount(
              amount.to_f, 
              CurrencySymbolToCode[currency_symbol]
            )
          )
        else
          raise ArgumentError, "invalid chip amount `#{string}`"
        end
      end
    end
  
    def initialize(amount)
      @amount = amount
    end
  end

  module Street
    PreFlop = :PreFlop
    Flop    = :Flop
    Turn    = :Turn
    River   = :River
    
    module_function
      def from_string(string)
        case string
        when 'FLOP'; Flop
        when 'TURN'; Turn
        when 'RIVER'; River
        else
          raise IllegalArgumentException, "Invalid street: `#{string}`"
        end
      end
  end

  module Suit
    Club    = :Club
    Diamond = :Diamond
    Heart   = :Heart
    Spade   = :Spade
  end

  module Rank
    Ace   = 14
    King  = 13
    Queen = 12
    Jack  = 11
    Ten   = 10
  end
}

class CardShorthandTranslater
  ShortHandToRank = {
    'A' => Rank::Ace,
    'K' => Rank::King,
    'Q' => Rank::Queen,
    'J' => Rank::Jack,
    'T' => Rank::Ten
  }
  
  ShortHandToRank.default_proc = ->(h, k) {
    if k.to_i < 11 && k.to_i > 1
      h[k] = k.to_i
    else
      ShortHandToRank[k]
    end
  }
  
  ShortHandToSuit = {
    'c' => Suit::Club,
    'd' => Suit::Diamond,
    'h' => Suit::Heart,
    's' => Suit::Spade
  }
  
  def self.translate(shorthand)
    return nil unless shorthand
    raise IllegalArgumentException unless shorthand.size == 2
    rank, suit = *shorthand.split(//)
    
    Card.new(
      ShortHandToRank[rank],
      ShortHandToSuit[suit]
    )
  end
end

if __FILE__ == $0
  def row(hand)
    h = hand
    collect_action = h.actions.detect {|a| a.action.is_a?(Action::Collect)}
    return nil unless collect_action
    won_on = collect_action.street
    winner = collect_action.player.screen_name
    initial_stack = collect_action.player.initial_stack.amount.to_f
    total_pot = h.total_pot.amount.to_f
    bets = h.actions.select {|a| a.action.is_a?(Action::Bet)}
    bets_by_winner = bets.size
    raises = h.actions.select {|a| a.action.is_a?(Action::Raise)}
    raises_by_winner = raises.size

    is_button = collect_action.player.screen_name == h.seat_assignments.button.player.screen_name
    bets_amount = bets.map(&:action).map(&:amount).map(&:amount).map(&:to_f).inject(&:+) || 0
    raises_amount = raises.map(&:action).map {|r| r.to.amount - r.from.amount}.map(&:to_f).inject(&:+) || 0
    calls_by_winner = h.actions.select {|a| a.action.is_a?(Action::Call)}.size
    checks_by_winner = h.actions.select {|a| a.action.is_a?(Action::Check)}.size
    row = [won_on.to_s, winner, total_pot, is_button, bets_by_winner, bets_amount, raises_by_winner, raises_amount, calls_by_winner, checks_by_winner, initial_stack, h.hand_id]
  end

  headers = %w[street winner total_pot is_button bets bets_amount raises raises_amount calls checks initial_stack, id]

  skip_headers = if !ARGV[0].nil? && ARGV[0] == '--no-headers'
    ARGV.shift
    true
  else false
  end
  
  working_dir = `pwd`.chomp
  
  rows = []
  
  ARGV.each do |file|
    hh = HandParser.from_file(File.join(working_dir, file))
    hh.map {|r| row(r)}.compact.each do |hand|
      rows << hand
    end
  end
  
  csv = CSV.generate(headers: headers, write_headers: !skip_headers) {|o| rows.each {|r|o << r}}
  puts csv
end