/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mytunes.dal;

/**
 *
 * @author janvanzetten
 */
public class DALManager
{
    DAO dao;

    public DALManager() throws DALException
    {
        dao = new DatabaseDAO();
    }

    public DAO getDAO()
    {
        return dao;
    }
}
