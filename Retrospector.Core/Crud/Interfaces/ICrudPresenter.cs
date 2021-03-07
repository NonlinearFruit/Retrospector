using System.Collections.Generic;

namespace Retrospector.Core.Crud.Interfaces
{
    public interface ICrudPresenter<T> where T : IModel
    {
        public void Added(T model);
        public void Retrieved(T model);
        public void Updated(T model);
        public void Deleted(int id);
        public void RetrievedAll(IEnumerable<T> models);
        public void RetrievedByMediaId(IEnumerable<T> models);
    }
}