using Retrospector.Core.Boundary;

namespace Retrospector.Core.Crud.Models
{
    public class CrudRequest<T> : IRequest
    {
        public CrudEnum Crud{ get; set; }
        public T Model{ get; set; }
        public int ModelId{ get; set; }
    }
}