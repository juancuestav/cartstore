class DireccionController < ApplicationController
    def add_direccion
        # Variables
        dir_direccion = params[:dir_direccion].upcase        
        dir_referencia = params[:dir_referencia].upcase
        dir_telefono = params[:dir_telefono]
        dir_usu_id = params[:dir_usu_id]
        
        begin
            plsql.registrar_direccion(dir_direccion, dir_referencia, dir_telefono, dir_usu_id)            
        rescue OCIError => oracle_error
            if oracle_error.code.to_s == '1'                
                render json: {error: "Ya existe una dirección con los mismos datos"}
            end
        else            
            render json: {exito: "Dirección registrada"}
        end
    end

    def get_direcciones
        direcciones = []
        plsql.obtener_direcciones(params[:usu_id].to_i) do |cursor|   
            cursor.fetch_all.each do |value|
                direccion = {
                    :dir_id => '',
                    :dir_direccion => '',                     
                    :dir_referencia => '',
                    :dir_telefono => ''
                }            
                direccion[:dir_id] = value[0]
                direccion[:dir_direccion] = value[1]                  
                direccion[:dir_referencia] = value[2] 
                direccion[:dir_telefono] = value[3]     
                direcciones.append(direccion)
            end
        end
        render json: direcciones.to_json
    end

    def update_direccion
        # Variables
        dir_id = params[:dir_id]
        dir_direccion = params[:dir_direccion].upcase        
        dir_referencia = params[:dir_referencia].upcase        
        dir_telefono = params[:dir_telefono]
        
        plsql.actualizar_direccion(dir_id.to_i, dir_direccion, dir_referencia, dir_telefono)
        render json: {exito: "Direccion actualizada"}    
    end

    def delete_direccion
        # Variables
        dir_id = params[:dir_id]
        
        plsql.eliminar_direccion(dir_id.to_i)
        render json: {exito: "Direccion eliminada"}    
    end
end