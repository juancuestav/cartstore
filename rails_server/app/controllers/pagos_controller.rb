class PagosController < ApplicationController
    def add_pago
        # Variables
        pago_numero_tarj = params[:pago_numero_tarj]
        pago_nombre_tarj = params[:pago_nombre_tarj].upcase
        pago_mes_venc = params[:pago_mes_venc]
        pago_anio_venc = params[:pago_anio_venc]
        pago_usu_id = params[:pago_usu_id]
        
        begin
            plsql.registrar_pago(pago_numero_tarj, pago_nombre_tarj, pago_mes_venc, pago_anio_venc, pago_usu_id)           
        rescue OCIError => oracle_error
            if oracle_error.code.to_s == '20002'                
                render json: {error: "Ha cumplido la fecha de vencimiento"}
            end
            if oracle_error.code.to_s == '1'                
                render json: {error: "Ya tiene un metodo de pago registrado"}
            end
        else 
            render json: {exito: "Metodo de pago registrado"}   
        end 
    end

    def get_pagos
        pagos = []
        plsql.obtener_pagos(params[:usu_id].to_i) do |cursor|   
            cursor.fetch_all.each do |value|
                puts value[2]
                pago = {
                    :pago_numero_tarj => '',
                    :pago_nombre_tarj => '',
                    :pago_mes_venc => '', 
                    :pago_anio_venc => ''
                }            
                pago[:pago_numero_tarj] = value[1]
                pago[:pago_nombre_tarj] = value[2]       
                pago[:pago_mes_venc] = value[3]
                pago[:pago_anio_venc] = value[4]
                pagos.append(pago)
            end
        end
        render json: pagos.to_json
    end

    def update_pago
        # Variables
        pago_id = params[:pago_id]
        pago_numero_tarj = params[:pago_numero_tarj]
        pago_nombre_tarj = params[:pago_nombre_tarj].upcase
        pago_mes_venc = params[:pago_mes_venc]
        pago_anio_venc = params[:pago_anio_venc]
        
        plsql.actualizar_pago(pago_id.to_i, pago_numero_tarj, pago_nombre_tarj, pago_mes_venc, pago_anio_venc)
        render json: {exito: "Metodo de pago actualizado"}    
    end

    def delete_pago
        # Variables
        pago_id = params[:pago_id]
        
        plsql.eliminar_pago(pago_id.to_i)
        render json: {exito: "Metodo de pago eliminado"}    
    end
end