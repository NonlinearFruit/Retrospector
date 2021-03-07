using System.Collections.Generic;

namespace Retrospector.Core.Crud.Interfaces
{
    public interface ICrudDataGateway<T>
    {
        public T Add(T model);
        public T Get(int id);
        public T Update(T model);
        public void Delete(int id);
        public IEnumerable<T> GetAll();
        public IEnumerable<T> GetByMediaId(int mediaId);
    }
}