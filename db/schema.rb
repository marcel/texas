# encoding: UTF-8
# This file is auto-generated from the current state of the database. Instead
# of editing this file, please use the migrations feature of Active Record to
# incrementally modify your database, and then regenerate this schema definition.
#
# Note that this schema.rb definition is the authoritative source for your
# database schema. If you need to create the application database on another
# system, you should be using db:schema:load, not running all the migrations
# from scratch. The latter is a flawed and unsustainable approach (the more migrations
# you'll amass, the slower it'll run and the greater likelihood for issues).
#
# It's strongly recommended that you check this file into your version control system.

ActiveRecord::Schema.define(version: 20150728170825) do

  create_table "hand_histories", force: :cascade do |t|
    t.decimal  "hand_id",               precision: 20
    t.decimal  "main_pot",              precision: 10, scale: 2
    t.decimal  "side_pot",              precision: 10, scale: 2
    t.integer  "big_blind",   limit: 4
    t.integer  "small_blind", limit: 4
    t.datetime "played_at"
  end

  create_table "player_actions", force: :cascade do |t|
    t.integer "hand_id",   limit: 4
    t.string  "user_name", limit: 255
    t.integer "number",    limit: 4
    t.string  "action",    limit: 255
    t.decimal "from",                  precision: 10, scale: 2
    t.decimal "to",                    precision: 10, scale: 2
    t.decimal "amount",                precision: 10, scale: 2
    t.string  "street",    limit: 255
  end

  add_index "player_actions", ["hand_id"], name: "index_player_actions_on_hand_id", using: :btree

end
