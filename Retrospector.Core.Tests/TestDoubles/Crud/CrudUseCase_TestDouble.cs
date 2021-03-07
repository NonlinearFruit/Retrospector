using Retrospector.Core.Boundary;
using Retrospector.Core.Crud.Interfaces;
using Retrospector.Core.Crud.Models;

namespace Retrospector.Core.Tests.TestDoubles.Crud
{
    public class CrudUseCase_TestDouble<T> : IUseCase<CrudRequest<T>> where T : IModel
    {
        public int CountOfCallsTo_Execute { get; set; }
        public CrudRequest<T> LastRequestPassedTo_Execute { get; set; }
        public void Execute(CrudRequest<T> request)
        {
            CountOfCallsTo_Execute++;
            LastRequestPassedTo_Execute = request;
        }
    }
}