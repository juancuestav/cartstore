Rails.application.routes.draw do
  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
    post '/direccion' => 'direccion#add_direccion'
    get '/direccion/:usu_id' => 'direccion#get_direcciones'
    put '/direccion/:dir_id' => 'direccion#update_direccion'
    delete '/direccion/:dir_id' => 'direccion#delete_direccion'

    post '/pago' => 'pagos#add_pago'
    get '/pago/:usu_id' => 'pagos#get_pagos'
    put '/pago/:pago_usu_id' => 'pagos#update_pago'
    delete '/pago/:pago_usu_id' => 'pagos#delete_pago'
end
