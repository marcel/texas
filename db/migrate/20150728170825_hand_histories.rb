class HandHistories < ActiveRecord::Migration
  def change
    create_table :hand_histories do |t|
      t.decimal :hand_id, precision: 20, scale: 0
      t.decimal :main_pot, precision: 10, scale: 2
      t.decimal :side_pot, precision: 10, scale: 2
      t.integer :big_blind
      t.integer :small_blind
      t.datetime :played_at
    end
    
    create_table :player_actions do |t|
      t.decimal :hand_id, precision: 20, scale: 0
      t.string :user_name
      t.integer :number
      t.belongs_to :hand, index: true
      t.string :action
      t.decimal :from, precision: 10, scale: 2
      t.decimal :to, precision: 10, scale: 2
      t.decimal :amount, precision: 10, scale: 2
      t.string :street
    end
  end
end
