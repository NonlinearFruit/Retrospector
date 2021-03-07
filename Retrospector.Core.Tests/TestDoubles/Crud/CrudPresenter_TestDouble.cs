using System.Collections.Generic;
using Retrospector.Core.Crud.Interfaces;

namespace Retrospector.Core.Tests.TestDoubles.Crud
{
    public class CrudPresenter_TestDouble<T> : ICrudPresenter<T> where T : IModel
    {
        public int CountOf_Added_Calls{ get; set; }
        public T LastModelPassedTo_Added{ get; set; }
        public void Added(T model)
        {
            CountOf_Added_Calls++;
            LastModelPassedTo_Added = model;
        }

        public T LastModelPassedTo_Retrieved { get; set; }
        public int CountOf_Retrieved_Calls { get; set; }
        public void Retrieved(T model)
        {
            CountOf_Retrieved_Calls++;
            LastModelPassedTo_Retrieved = model;
        }

        public T LastModelPassedTo_Updated { get; set; }
        public int CountOf_Updated_Calls { get; set; }
        public void Updated(T model)
        {
            CountOf_Updated_Calls++;
            LastModelPassedTo_Updated = model;
        }

        public int LastIdPassedTo_Deleted { get; set; }
        public int CountOf_Deleted_Calls { get; set; }
        public void Deleted(int id)
        {
            CountOf_Deleted_Calls++;
            LastIdPassedTo_Deleted = id;
        }

        public IEnumerable<T> LastModelsPassedTo_RetrievedAll { get; set; }
        public int CountOf_RetrievedAll_Calls { get; set; }
        public void RetrievedAll(IEnumerable<T> models)
        {
            CountOf_RetrievedAll_Calls++;
            LastModelsPassedTo_RetrievedAll = models;
        }

        public IEnumerable<T> LastModelsPassedTo_RetrievedByMediaId { get; set; }
        public int CountOf_RetrievedByMediaId_Calls { get; set; }
        public void RetrievedByMediaId(IEnumerable<T> models)
        {
            CountOf_RetrievedByMediaId_Calls++;
            LastModelsPassedTo_RetrievedByMediaId = models;
        }
    }
}