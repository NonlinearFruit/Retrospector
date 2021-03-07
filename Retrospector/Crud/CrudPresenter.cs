using System.Collections.Generic;
using Retrospector.Core.Crud.Interfaces;

namespace Retrospector.Crud
{
    public class CrudPresenter<T> : ICrudPresenter<T> where T : IModel
    {
        public void Added(T model)
        {
            throw new System.NotImplementedException();
        }

        public void Retrieved(T model)
        {
            throw new System.NotImplementedException();
        }

        public void Updated(T model)
        {
            throw new System.NotImplementedException();
        }

        public void Deleted(int id)
        {
            throw new System.NotImplementedException();
        }

        public void RetrievedAll(IEnumerable<T> models)
        {
            throw new System.NotImplementedException();
        }

        public void RetrievedByMediaId(IEnumerable<T> models)
        {
            throw new System.NotImplementedException();
        }
    }
}