using System.Collections.Generic;
using Retrospector.Core.Crud.Interfaces;

namespace Retrospector.Core.Tests.TestDoubles.Crud
{
    public class CrudDataGateway_TestDouble<T> : ICrudDataGateway<T> where T : IModel
    {
        public int CountOf_Add_Calls{ get; set; }
        public T LastModelPassedTo_Add{ get; set; }
        public T ReturnFor_Add{ get; set; }
        public T Add(T model)
        {
            CountOf_Add_Calls++;
            LastModelPassedTo_Add = model;
            return ReturnFor_Add;
        }

        public T ReturnFor_Get { get; set; }
        public int LastIdPassedTo_Get { get; set; }
        public int CountOf_Get_Calls { get; set; }
        public T Get(int id)
        {
            CountOf_Get_Calls++;
            LastIdPassedTo_Get = id;
            return ReturnFor_Get;
        }

        public T ReturnFor_Update { get; set; }
        public T LastModelPassedTo_Update { get; set; }
        public int CountOf_Update_Calls { get; set; }
        public T Update(T model)
        {
            CountOf_Update_Calls++;
            LastModelPassedTo_Update = model;
            return ReturnFor_Update;
        }

        public int LastIdPassedTo_Delete { get; set; }
        public int CountOf_Delete_Calls { get; set; }
        public void Delete(int id)
        {
            CountOf_Delete_Calls++;
            LastIdPassedTo_Delete = id;
        }

        public int CountOf_GetAll_Calls { get; set; }
        public IEnumerable<T> ReturnFor_GetAll { get; set; }
        public IEnumerable<T> GetAll()
        {
            CountOf_GetAll_Calls++;
            return ReturnFor_GetAll;
        }

        public IEnumerable<T> ReturnFor_GetMediaById { get; set; }
        public int LastMediaIdPassedTo_GetByMediaId { get; set; }
        public int CountOf_GetByMediaId_Calls { get; set; }
        public IEnumerable<T> GetByMediaId(int mediaId)
        {
            CountOf_GetByMediaId_Calls++;
            LastMediaIdPassedTo_GetByMediaId = mediaId;
            return ReturnFor_GetMediaById;
        }
    }
}